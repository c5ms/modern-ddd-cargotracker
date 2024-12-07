package se.citerus.dddsample.domain.model.cargo;

import org.junit.jupiter.api.Test;
import se.citerus.dddsample.domain.model.location.Location;
import se.citerus.dddsample.domain.model.location.UnLocode;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class CargoTest {
    private static final Location FIHEL = Location.of(UnLocode.of("FIHEL"), "Helsinki");
    private static final Location CNSHA = Location.of(UnLocode.of("CNSHA"), "Shanghai");

    @Test
    void identity() {
        var arrivalDeadline = Instant.now().plus(1, ChronoUnit.DAYS);
        var cargo1 = Cargo.of(TrackingId.of("001"), FIHEL, CNSHA, arrivalDeadline);
        var cargo2 = Cargo.of(TrackingId.of("001"), FIHEL, CNSHA, arrivalDeadline);
        assertFalse(cargo1.sameIdentityAs(null));
        assertTrue(cargo1.sameIdentityAs(cargo2));
    }

    @Test
    void create() {
        var trackingId = TrackingId.of("001");
        var arrivalDeadline = Instant.now().plus(1, ChronoUnit.DAYS);
        var cargo = Cargo.of(trackingId, FIHEL, CNSHA, arrivalDeadline);

        assertEquals(0, cargo.getId());
        assertEquals(trackingId, cargo.getTrackingId());
        assertTrue(cargo.getItinerary().isEmpty());
        assertEquals(RoutingStatus.MIS_ROUTED, cargo.getDelivery().getRoutingStatus());
        assertEquals(FIHEL, cargo.getRouteSpecification().getOrigin());
        assertEquals(CNSHA, cargo.getRouteSpecification().getDestination());
        assertEquals(arrivalDeadline, cargo.getRouteSpecification().getArrivalDeadline());
        assertTrue(cargo.getItinerary().getLastLeg().isEmpty());
    }

    @Test
    void changeDestination() {
        var trackingId = TrackingId.of("001");
        var arrivalDeadline = Instant.now().plus(1, ChronoUnit.DAYS);
        var cargo = Cargo.of(trackingId, FIHEL, CNSHA, arrivalDeadline);

        var newDestination = Location.of(UnLocode.of("CNHGH"), "Hangzhou");
        cargo.changeDestination(newDestination);
        assertEquals(newDestination, cargo.getRouteSpecification().getDestination());
    }

}