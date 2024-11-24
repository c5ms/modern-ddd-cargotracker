package se.citerus.dddsample.domain.model.cargo;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.Validate;
import se.citerus.dddsample.domain.model.cargo.events.CargoCreatedEvent;
import se.citerus.dddsample.domain.model.cargo.events.CargoDestinationChangedEvent;
import se.citerus.dddsample.domain.model.cargo.events.CargoRoutedEvent;
import se.citerus.dddsample.domain.model.handling.HandlingHistory;
import se.citerus.dddsample.domain.model.location.Location;
import se.citerus.dddsample.domain.shared.AbstractEntity;
import se.citerus.dddsample.domain.shared.DomainEntity;
import se.citerus.dddsample.domain.shared.ImmutableValues;

import java.time.Instant;
import java.util.List;

@Entity(name = "Cargo")
@Table(name = "Cargo")
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Cargo extends AbstractEntity implements DomainEntity<Cargo> {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Nonnull
    @Column(name = "tracking_id", unique = true)
    private String trackingId;

    @Nonnull
    @Embedded
    private RouteSpecification routeSpecification;

    @Nonnull
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "cargo_id")
    private List<Leg> legs;

    @Nonnull
    @Embedded
    private Delivery delivery;

    private Cargo(@NonNull TrackingId trackingId,@NonNull RouteSpecification routeSpecification,@NonNull Itinerary itinerary) {
        Validate.notNull(trackingId, "trackingId is required");
        Validate.notNull(routeSpecification, "Route specification is required");
        this.trackingId = trackingId.getId();
        this.routeSpecification = routeSpecification;
        this.legs = itinerary.legs().toList();
        this.delivery = Delivery.derivedFrom(routeSpecification, itinerary, HandlingHistory.empty());
        this.addEvent(CargoCreatedEvent.of(this));
    }

    public static Cargo of(TrackingId trackingId, Location origin, Location destination, Instant arrivalDeadline) {
        Validate.notNull(trackingId, "trackingId is required");
        Validate.notNull(origin, "origin is required");
        Validate.notNull(destination, "destination is required");
        Validate.notNull(arrivalDeadline, "arrivalDeadline specification is required");

        var spec = RouteSpecification.of(origin, destination, arrivalDeadline);
        return new Cargo(trackingId, spec, Itinerary.empty());
    }


    /**
     * The tracking id is the identity of this entity, and is unique.
     *
     * @return Tracking id.
     */
    public TrackingId trackingId() {
        return TrackingId.of(trackingId);
    }



    /**
     * Updates all aspects of the cargo aggregate status
     * based on the current route specification, itinerary and handling of the cargo.
     * <p/>
     * When either of those three changes, i.e. when a new route is specified for the cargo,
     * the cargo is assigned to a route or when the cargo is handled, the status must be
     * re-calculated.
     * <p/>
     * {@link RouteSpecification} and {@link Itinerary} are both inside the Cargo
     * aggregate, so changes to them cause the status to be updated <b>synchronously</b>,
     * but changes to the delivery history (when a cargo is handled) cause the status update
     * to happen <b>asynchronously</b> since {@link se.citerus.dddsample.domain.model.handling.HandlingEvent} is in a different aggregate.
     *
     * @param handlingHistory handling history
     */
    public void deriveDeliveryProgress(final HandlingHistory handlingHistory) {
        // Delivery is a value object, so we can simply discard the old one
        // and replace it with a new
        this.delivery = Delivery.derivedFrom(getRouteSpecification(), itinerary(), handlingHistory.filterOnCargo(TrackingId.of(this.trackingId)));
    }

    /**
     * @return The itinerary. Never null.
     */
    public Itinerary itinerary() {
        if (CollectionUtils.isEmpty(legs)) {
            return Itinerary.empty();
        }
        if (legs == null || legs.isEmpty()) {
            return Itinerary.empty();
        }
        return Itinerary.of(legs);
    }


    /**
     * Attach a new itinerary to this cargo.
     *
     * @param itinerary an itinerary. May not be null.
     */
    public void assignToRoute(final Itinerary itinerary) {
        Validate.notNull(itinerary, "Itinerary is required for assignment");

        this.legs = itinerary.legs().toList();
        // Handling consistency within the Cargo aggregate synchronously
        this.delivery = delivery.updateOnRouting(this.routeSpecification, itinerary);
        this.addEvent(CargoRoutedEvent.of(this));
    }

    /**
     * Specifies a new route for this cargo.
     *
     * @param routeSpecification route specification.
     */
    private void specifyNewRoute(final RouteSpecification routeSpecification) {
        Validate.notNull(routeSpecification, "Route specification is required");

        this.routeSpecification = routeSpecification;
        Itinerary itineraryForRouting = this.itinerary();
        // Handling consistency within the Cargo aggregate synchronously
        this.delivery = delivery.updateOnRouting(this.routeSpecification, itineraryForRouting);
    }

    public void changeDestination(Location destination) {
        if (destination.equals(this.routeSpecification.getDestination())) {
            return;
        }
        this.specifyNewRoute(this.routeSpecification.withDestination(destination));
        this.addEvent(CargoDestinationChangedEvent.of(this));
    }


    public boolean isRouted() {
        if (this.delivery.getRoutingStatus() == RoutingStatus.MIS_ROUTED) {
            return false;
        }
        return !CollectionUtils.isEmpty(legs);
    }

    @Override
    public boolean sameIdentityAs(final Cargo other) {
        return other != null && trackingId.equals(other.trackingId);
    }

    public ImmutableValues<Leg> getLegs() {
        return ImmutableValues.of(legs);
    }
}
