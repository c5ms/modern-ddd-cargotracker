package com.github.c5ms.cargotracker.domain.model.cargo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import com.github.c5ms.cargotracker.domain.model.location.Location;
import com.github.c5ms.cargotracker.domain.model.location.UnLocode;
import com.github.c5ms.cargotracker.domain.model.voyage.Schedule;
import com.github.c5ms.cargotracker.domain.model.voyage.Voyage;
import com.github.c5ms.cargotracker.domain.model.voyage.VoyageNumber;
import org.junit.jupiter.api.Test;

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

		assertThat(cargo).hasNoNullFieldsOrProperties();
		assertEquals(trackingId, cargo.getTrackingId());
		assertTrue(cargo.getItinerary().isEmpty());
		assertEquals(RoutingStatus.NOT_ROUTED, cargo.getDelivery().getRoutingStatus());
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

	@Test
	void assignToRoute() {
		var trackingId = TrackingId.of("001");
		var arrivalDeadline = Instant.now().plus(1, ChronoUnit.DAYS);
		var cargo = Cargo.of(trackingId, FIHEL, CNSHA, arrivalDeadline);
		var itinerary = Itinerary.of(List.of(
				Leg.of(Voyage.of(VoyageNumber.of("V0001"), Schedule.empty()), FIHEL, CNSHA, Instant.now(), Instant.now())
		));
		cargo.assignToRoute(itinerary);
		assertEquals(RoutingStatus.ROUTED, cargo.getDelivery().getRoutingStatus());
	}
}