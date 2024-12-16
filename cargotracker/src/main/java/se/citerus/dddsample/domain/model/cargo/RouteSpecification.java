package se.citerus.dddsample.domain.model.cargo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import se.citerus.dddsample.domain.model.location.Location;
import se.citerus.dddsample.domain.shared.AbstractSpecification;
import se.citerus.dddsample.domain.shared.DomainValue;

import java.time.Instant;

/**
 * Route specification. Describes where a cargo origin and destination is,
 * and the arrival deadline.
 */
@Getter
@Embeddable
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RouteSpecification extends AbstractSpecification<Itinerary> implements DomainValue<RouteSpecification> {

    @ManyToOne
    @JoinColumn(name = "spec_origin_id")
    private Location origin;

    @ManyToOne()
    @JoinColumn(name = "spec_destination_id")
    private Location destination;

    @Column(name = "spec_arrival_deadline", nullable = false)
    private Instant arrivalDeadline;

    private RouteSpecification(final Location origin, final Location destination, final Instant arrivalDeadline) {
        Validate.notNull(origin, "Origin is required");
        Validate.notNull(destination, "Destination is required");
        Validate.notNull(arrivalDeadline, "Arrival deadline is required");
        Validate.isTrue(!origin.sameIdentityAs(destination), "Origin and destination can't be the same: " + origin);

        this.origin = origin;
        this.destination = destination;
        this.arrivalDeadline = arrivalDeadline;
    }

    /**
     * @param origin          origin location - can't be the same as the destination
     * @param destination     destination location - can't be the same as the origin
     * @param arrivalDeadline arrival deadline
     */
    public static RouteSpecification of(final Location origin, final Location destination, final Instant arrivalDeadline) {
        return new RouteSpecification(origin, destination, arrivalDeadline);
    }

    public RouteSpecification changeDestination(final Location destination) {
        return new RouteSpecification(origin, destination, arrivalDeadline);
    }

    @Override
    public boolean isSatisfiedBy(final Itinerary itinerary) {
        return itinerary != null &&
               origin.sameIdentityAs(itinerary.initialDepartureLocation()) &&
               destination.sameIdentityAs(itinerary.finalArrivalLocation()) &&
               arrivalDeadline.isAfter(itinerary.finalArrivalDate());
    }

    @Override
    public boolean sameValueAs(final RouteSpecification other) {
        return other != null && new EqualsBuilder().
            append(this.origin, other.origin).
            append(this.destination, other.destination).
            append(this.arrivalDeadline, other.arrivalDeadline).
            isEquals();
    }


}
