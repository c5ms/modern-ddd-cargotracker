package se.citerus.dddsample.domain.model.cargo;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.Validate;
import se.citerus.dddsample.domain.model.handling.HandlingEvent;
import se.citerus.dddsample.domain.model.location.Location;
import se.citerus.dddsample.domain.shared.ImmutableValues;
import se.citerus.dddsample.domain.shared.ValueObject;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An itinerary.
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Itinerary implements ValueObject<Itinerary> {

    private static final Instant END_OF_DAYS = Instant.MAX;

    private List<Leg> legs = new ArrayList<>();

    private Itinerary(final List<Leg> legs) {
        Validate.notEmpty(legs);
        Validate.noNullElements(legs);

        this.legs = legs;
    }

    public static Itinerary empty(){
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
            return (leg.loadLocation().equals(event.getLocation()));
        }

        if (event.getType() == HandlingEvent.Type.LOAD) {
            //Check that the there is one leg with same load location and voyage
            for (Leg leg : legs) {
                if (leg.loadLocation().sameIdentityAs(event.getLocation()) &&
                    leg.voyage().sameIdentityAs(event.getVoyage()))
                    return true;
            }
            return false;
        }

        if (event.getType() == HandlingEvent.Type.UNLOAD) {
            //Check that the there is one leg with same unload location and voyage
            for (Leg leg : legs) {
                if (leg.unloadLocation().equals(event.getLocation()) &&
                    leg.voyage().equals(event.getVoyage()))
                    return true;
            }
            return false;
        }

        if (event.getType() == HandlingEvent.Type.CLAIM) {
            //Check that the last leg's destination is from the event's location
            final Leg leg = lastLeg();
            return (leg.unloadLocation().equals(event.getLocation()));
        }

        //HandlingEvent.Type.CUSTOMS;
        return true;
    }

    /**
     * @return The initial departure location.
     */
    Location initialDepartureLocation() {
        if (legs.isEmpty()) {
            return Location.unknown();
        } else {
            return legs.get(0).loadLocation();
        }
    }

    /**
     * @return The final arrival location.
     */
    Location finalArrivalLocation() {
        if (legs.isEmpty()) {
            return Location.unknown();
        } else {
            return lastLeg().unloadLocation();
        }
    }

    /**
     * @return Date when cargo arrives at final destination.
     */
    Instant finalArrivalDate() {
        final Leg lastLeg = lastLeg();

        if (lastLeg == null) {
            return END_OF_DAYS;
        } else {
            return lastLeg.unloadTime();
        }
    }

    /**
     * @return The last leg on the itinerary.
     */
    Leg lastLeg() {
        if (legs.isEmpty()) {
            return null;
        } else {
            return legs.get(legs.size() - 1);
        }
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
