package com.github.c5ms.cargotracker.domain.model.cargo;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.Validate;
import com.github.c5ms.cargotracker.domain.model.cargo.events.CargoCreatedEvent;
import com.github.c5ms.cargotracker.domain.model.cargo.events.CargoDestinationChangedEvent;
import com.github.c5ms.cargotracker.domain.model.cargo.events.CargoRoutedEvent;
import com.github.c5ms.cargotracker.domain.model.handling.HandlingHistory;
import com.github.c5ms.cargotracker.domain.model.location.Location;
import com.github.c5ms.cargotracker.domain.shared.AbstractEntity;
import com.github.c5ms.cargotracker.domain.shared.DomainEntity;

import java.time.Instant;

@Entity(name = "Cargo")
@Table(name = "Cargo")
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Cargo extends AbstractEntity implements DomainEntity<Cargo> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Nonnull
    @Embedded
    private TrackingId trackingId;

    @Nonnull
    @Embedded
    private RouteSpecification routeSpecification;

    @Nonnull
    @Embedded
    private Delivery delivery;

    @Nonnull
    @Embedded
    private Itinerary itinerary;

    private Cargo(@NonNull TrackingId trackingId, @NonNull RouteSpecification routeSpecification, @NonNull Itinerary itinerary) {
        Validate.notNull(trackingId, "trackingId is required");
        Validate.notNull(routeSpecification, "Route specification is required");
        this.trackingId = trackingId;
        this.routeSpecification = routeSpecification;
        this.delivery = Delivery.derivedFrom(routeSpecification, itinerary, HandlingHistory.empty());
        this.itinerary = itinerary;
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

    public void deriveDeliveryProgress(final HandlingHistory handlingHistory) {
        this.delivery = Delivery.derivedFrom(getRouteSpecification(),
            getItinerary(),
            handlingHistory.filterOnCargo(getTrackingId()));
    }

    public void assignToRoute(final Itinerary itinerary) {
        Validate.notNull(itinerary, "Itinerary is required for assignment");
        this.delivery = delivery.updateOnRouting(this.routeSpecification, itinerary);
        this.addEvent(CargoRoutedEvent.of(this));
    }

    public void changeDestination(Location destination) {
        Validate.notNull(destination, "destination is required");
        if (destination.equals(this.routeSpecification.getDestination())) {
            return;
        }
        this.routeSpecification = this.routeSpecification.changeDestination(destination);
        this.delivery = delivery.updateOnRouting(this.routeSpecification, this.itinerary);
        this.addEvent(CargoDestinationChangedEvent.of(this));
    }

    @Override
    public boolean sameIdentityAs(final Cargo other) {
        if (null == other) {
            return false;
        }
        return trackingId.equals(other.trackingId);
    }
}
