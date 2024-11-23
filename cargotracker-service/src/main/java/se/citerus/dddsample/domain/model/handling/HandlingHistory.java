package se.citerus.dddsample.domain.model.handling;

import org.apache.commons.lang3.Validate;
import se.citerus.dddsample.domain.model.cargo.TrackingId;
import se.citerus.dddsample.domain.shared.ValueObject;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The handling history of a cargo.
 */
public class HandlingHistory implements ValueObject<HandlingHistory> {

    private final List<HandlingEvent> handlingEvents;

    public HandlingHistory(Collection<HandlingEvent> handlingEvents) {
        Validate.notNull(handlingEvents, "Handling events are required");

        this.handlingEvents = new ArrayList<>(handlingEvents);
    }

    public  static HandlingHistory empty() {
        return new HandlingHistory(new ArrayList<>());
    }

    /**
     * @return A distinct list (no duplicate registrations) of handling events, ordered by completion time.
     */
    public List<HandlingEvent> distinctEventsByCompletionTime() {
        final List<HandlingEvent> ordered = new ArrayList<>(
            new HashSet<>(handlingEvents)
        );
        ordered.sort(Comparator.comparing(HandlingEvent::getCompletionTime));
        return Collections.unmodifiableList(ordered);
    }

    /**
     * @return Most recently completed event, or null if the delivery history is empty.
     */
    public HandlingEvent mostRecentlyCompletedEvent() {
        final List<HandlingEvent> distinctEvents = distinctEventsByCompletionTime();
        if (distinctEvents.isEmpty()) {
            return null;
        } else {
            return distinctEvents.get(distinctEvents.size() - 1);
        }
    }

    /**
     * Filters handling history events to remove events for unrelated cargo.
     *
     * @param trackingId the trackingId of the cargo to filter events for.
     * @return A new handling history with events matching the supplied tracking id.
     */
    public HandlingHistory filterOnCargo(TrackingId trackingId) {
        List<HandlingEvent> events = handlingEvents.stream()
            .filter(he -> he.getCargo().trackingId().sameValueAs(trackingId))
            .collect(Collectors.toList());
        return new HandlingHistory(events);
    }

    @Override
    public boolean sameValueAs(HandlingHistory other) {
        return other != null && this.handlingEvents.equals(other.handlingEvents);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final HandlingHistory other = (HandlingHistory) o;
        return sameValueAs(other);
    }

    @Override
    public int hashCode() {
        return handlingEvents.hashCode();
    }

}
