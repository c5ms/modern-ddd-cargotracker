package se.citerus.dddsample.domain.model.cargo;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import se.citerus.dddsample.domain.model.handling.HandlingEvent;
import se.citerus.dddsample.domain.model.handling.HandlingHistory;
import se.citerus.dddsample.domain.model.location.Location;
import se.citerus.dddsample.domain.model.voyage.Voyage;
import se.citerus.dddsample.domain.shared.ValueObject;

import java.time.Instant;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;

import static se.citerus.dddsample.domain.model.cargo.RoutingStatus.*;
import static se.citerus.dddsample.domain.model.cargo.TransportStatus.*;

/**
 * The actual transportation of the cargo, as opposed to
 * the customer requirement (RouteSpecification) and the plan (Itinerary).
 */
@Getter
@Embeddable
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery implements ValueObject<Delivery> {

    private static final Instant ETA_UNKNOWN = null;
    private static final HandlingActivity NO_ACTIVITY = null;

    @Column
    private boolean misdirected;

    @Column
    private Instant eta;

    @Column(name = "calculated_at")
    private Instant calculatedAt;

    @Column(name = "unloaded_at_dest")
    private boolean isUnloadedAtDestination;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "routing_status")
    private RoutingStatus routingStatus;

    @Embedded
    private HandlingActivity nextExpectedActivity;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "transport_status")
    private TransportStatus transportStatus;

    @ManyToOne
    @JoinColumn(name = "current_voyage_id")
    private Voyage currentVoyage;

    @ManyToOne
    @JoinColumn(name = "last_known_location_id")
    private Location lastKnownLocation;

    @ManyToOne
    @JoinColumn(name = "last_event_id")
    private HandlingEvent lastEvent;

    /**
     * Internal constructor.
     *
     * @param lastEvent          last event
     * @param itinerary          itinerary
     * @param routeSpecification route specification
     */
    private Delivery(HandlingEvent lastEvent, Itinerary itinerary, RouteSpecification routeSpecification) {
        this.calculatedAt = Instant.now();
        this.lastEvent = lastEvent;

        this.misdirected = calculateMisdirectionStatus(itinerary);
        this.routingStatus = calculateRoutingStatus(itinerary, routeSpecification);
        this.transportStatus = calculateTransportStatus();
        this.lastKnownLocation = calculateLastKnownLocation();
        this.currentVoyage = calculateCurrentVoyage();
        this.eta = calculateEta(itinerary);
        this.nextExpectedActivity = calculateNextExpectedActivity(routeSpecification, itinerary);
        this.isUnloadedAtDestination = calculateUnloadedAtDestination(routeSpecification);
    }

    /**
     * Creates a new delivery snapshot based on the complete handling history of a cargo,
     * as well as its route specification and itinerary.
     *
     * @param routeSpecification route specification
     * @param itinerary          itinerary
     * @param handlingHistory    delivery history
     * @return An up to date delivery.
     */
    public static Delivery derivedFrom(RouteSpecification routeSpecification, Itinerary itinerary, HandlingHistory handlingHistory) {
        Validate.notNull(routeSpecification, "Route specification is required");
        Validate.notNull(handlingHistory, "Delivery history is required");

        final HandlingEvent lastEvent = handlingHistory.mostRecentlyCompletedEvent();

        return new Delivery(lastEvent, itinerary, routeSpecification);
    }

    /**
     * Creates a new delivery snapshot to reflect changes in routing, i.e.
     * when the route specification or the itinerary has changed
     * but no additional handling of the cargo has been performed.
     *
     * @param routeSpecification route specification
     * @param itinerary          itinerary
     * @return An up to date delivery
     */
    Delivery updateOnRouting(RouteSpecification routeSpecification, Itinerary itinerary) {
        Validate.notNull(routeSpecification, "Route specification is required");

        return new Delivery(this.lastEvent, itinerary, routeSpecification);
    }

    /**
     * @return Transport status
     */
    public TransportStatus transportStatus() {
        return transportStatus;
    }

    /**
     * @return Last known location of the cargo, or Location.UNKNOWN if the delivery history is empty.
     */
    public Location lastKnownLocation() {
        return Objects.requireNonNullElse(lastKnownLocation, Location.UNKNOWN);
    }

    /**
     * @return Current voyage.
     */
    public Voyage currentVoyage() {
        return Objects.requireNonNullElse(currentVoyage, Voyage.empty());
    }


    /**
     * @return Estimated time of arrival
     */
    public Instant estimatedTimeOfArrival() {
        if (eta != ETA_UNKNOWN) {
            return eta;
        } else {
            return ETA_UNKNOWN;
        }
    }

    private TransportStatus calculateTransportStatus() {
        if (lastEvent == null) {
            return NOT_RECEIVED;
        }
        return switch (lastEvent.getType()) {
            case LOAD -> ONBOARD_CARRIER;
            case UNLOAD, RECEIVE, CUSTOMS -> IN_PORT;
            case CLAIM -> CLAIMED;
            default -> UNKNOWN;
        };
    }

    private Location calculateLastKnownLocation() {
        if (lastEvent != null) {
            return lastEvent.getLocation();
        } else {
            return null;
        }
    }

    private Voyage calculateCurrentVoyage() {
        if (transportStatus().equals(ONBOARD_CARRIER) && lastEvent != null) {
            return lastEvent.getVoyage();
        } else {
            return null;
        }
    }

    private boolean calculateMisdirectionStatus(Itinerary itinerary) {
        if (lastEvent == null) {
            return false;
        } else {
            return !itinerary.isExpected(lastEvent);
        }
    }

    private Instant calculateEta(Itinerary itinerary) {
        if (onTrack()) {
            return itinerary.finalArrivalDate();
        } else {
            return ETA_UNKNOWN;
        }
    }

    private HandlingActivity calculateNextExpectedActivity(RouteSpecification routeSpecification, Itinerary itinerary) {
        if (!onTrack()) return NO_ACTIVITY;

        if (lastEvent == null) return new HandlingActivity(HandlingEvent.Type.RECEIVE, routeSpecification.getOrigin());

        return switch (lastEvent.getType()) {
            case LOAD -> {
                for (Leg leg : itinerary.legs()) {
                    if (leg.getLoadLocation().sameIdentityAs(lastEvent.getLocation())) {
                        yield new HandlingActivity(HandlingEvent.Type.UNLOAD, leg.getUnloadLocation(), leg.getVoyage());
                    }
                }

                yield NO_ACTIVITY;
            }
            case UNLOAD -> {
                for (Iterator<Leg> it = itinerary.legs().iterator(); it.hasNext(); ) {
                    final Leg leg = it.next();
                    if (leg.getUnloadLocation().sameIdentityAs(lastEvent.getLocation())) {
                        if (it.hasNext()) {
                            final Leg nextLeg = it.next();
                            yield new HandlingActivity(HandlingEvent.Type.LOAD, nextLeg.getLoadLocation(), nextLeg.getVoyage());
                        } else {
                            yield new HandlingActivity(HandlingEvent.Type.CLAIM, leg.getUnloadLocation());
                        }
                    }
                }

                yield NO_ACTIVITY;
            }
            case RECEIVE -> {
                final Leg firstLeg = itinerary.legs().iterator().next();
                yield new HandlingActivity(HandlingEvent.Type.LOAD, firstLeg.getLoadLocation(), firstLeg.getVoyage());
            }
            default -> NO_ACTIVITY;
        };
    }

    private RoutingStatus calculateRoutingStatus(Itinerary itinerary, RouteSpecification routeSpecification) {
        if (itinerary == null) {
            return NOT_ROUTED;
        } else {
            if (routeSpecification.isSatisfiedBy(itinerary)) {
                return ROUTED;
            } else {
                return MIS_ROUTED;
            }
        }
    }

    private boolean calculateUnloadedAtDestination(RouteSpecification routeSpecification) {
        return lastEvent != null
               && HandlingEvent.Type.UNLOAD == lastEvent.getType()
               && routeSpecification.getDestination().sameIdentityAs(lastEvent.getLocation());
    }

    private boolean onTrack() {
        return routingStatus.equals(ROUTED) && !misdirected;
    }

    public Optional<Voyage> getCurrentVoyage() {
        return Optional.ofNullable(currentVoyage);
    }

    @Override
    public boolean sameValueAs(final Delivery other) {
        return other != null && new EqualsBuilder().
            append(this.transportStatus, other.transportStatus).
            append(this.lastKnownLocation, other.lastKnownLocation).
            append(this.currentVoyage, other.currentVoyage).
            append(this.misdirected, other.misdirected).
            append(this.eta, other.eta).
            append(this.nextExpectedActivity, other.nextExpectedActivity).
            append(this.isUnloadedAtDestination, other.isUnloadedAtDestination).
            append(this.routingStatus, other.routingStatus).
            append(this.calculatedAt, other.calculatedAt).
            append(this.lastEvent, other.lastEvent).
            isEquals();
    }


}
