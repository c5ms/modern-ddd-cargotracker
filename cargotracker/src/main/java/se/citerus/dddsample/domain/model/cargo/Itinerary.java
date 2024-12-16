package se.citerus.dddsample.domain.model.cargo;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.Validate;
import se.citerus.dddsample.domain.model.handling.HandlingEvent;
import se.citerus.dddsample.domain.model.location.Location;
import se.citerus.dddsample.domain.shared.ImmutableValues;
import se.citerus.dddsample.domain.shared.DomainValue;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * An itinerary.
 */
@Embeddable
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Itinerary implements DomainValue<Itinerary> {

    private static final Instant END_OF_DAYS = Instant.MAX;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cargo_id")
    @OrderColumn(name = "leg_order")
    private List<Leg> legs = Collections.emptyList();

    private Itinerary(final List<Leg> legs) {
        Validate.notEmpty(legs);
        Validate.noNullElements(legs);

        this.legs = legs;
    }

    public static Itinerary empty() {
        return new Itinerary();
    }

    public static Itinerary of(List<Leg> legs) {
        return new Itinerary(legs);
    }

    /**
     * @return the legs of this itinerary, as an <b>immutable</b> list.
     */
    public ImmutableValues<Leg> legs() {
        return ImmutableValues.of(legs);
    }

    public boolean isEmpty() {
        return this.legs.isEmpty();
    }

    /**
     * Test if the given handling event is expected when executing this itinerary.
     *
     * @param event Event to test.
     * @return <code>true</code> if the event is expected
     */
    public boolean isExpected(final HandlingEvent event) {
        if (legs.isEmpty()) {
            return true;
        }

        if (event.getType() == HandlingEvent.Type.RECEIVE) {
            //Check that the first leg's origin is the event's location
            final Leg leg = legs.get(0);
            return (leg.getLoadLocation().equals(event.getLocation()));
        }

        if (event.getType() == HandlingEvent.Type.LOAD) {
            //Check that the there is one leg with same load location and voyage
            for (Leg leg : legs) {
                if (leg.getLoadLocation().sameIdentityAs(event.getLocation()) &&
                    leg.getVoyage().sameIdentityAs(event.getVoyage()))
                    return true;
            }
            return false;
        }

        if (event.getType() == HandlingEvent.Type.UNLOAD) {
            //Check that the there is one leg with same unload location and voyage
            for (Leg leg : legs) {
                if (leg.getUnloadLocation().equals(event.getLocation()) &&
                    leg.getVoyage().equals(event.getVoyage()))
                    return true;
            }
            return false;
        }

        if (event.getType() == HandlingEvent.Type.CLAIM) {
            //Check that the last leg's destination is from the event's location
            return getLastLeg()
                .map(Leg::getUnloadLocation)
                .map(event.getLocation()::equals)
                .orElse(false);
        }

        //HandlingEvent.Type.CUSTOMS;
        return true;
    }

    /**
     * @return The initial departure location.
     */
    Location initialDepartureLocation() {
        return getLastLeg()
            .map(Leg::getLoadLocation)
            .orElse(Location.UNKNOWN);
    }

    /**
     * @return The final arrival location.
     */
    Location finalArrivalLocation() {
        return getLastLeg()
            .map(Leg::getUnloadLocation)
            .orElse(Location.UNKNOWN);
    }

    /**
     * @return Date when cargo arrives at final destination.
     */
    Instant finalArrivalDate() {
        return getLastLeg()
            .map(Leg::getUnloadTime)
            .orElse(END_OF_DAYS);
    }

    /**
     * @return The last leg on the itinerary.
     */
    Optional<Leg> getLastLeg() {
        if (legs.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(legs.get(legs.size() - 1));
    }

    /**
     * @param other itinerary to compare
     * @return <code>true</code> if the legs in this and the other itinerary are all equal.
     */
    @Override
    public boolean sameValueAs(final Itinerary other) {
        return other != null && legs.equals(other.legs);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Itinerary itinerary = (Itinerary) o;

        return sameValueAs(itinerary);
    }

    @Override
    public int hashCode() {
        return legs.hashCode();
    }
}
