package com.github.c5ms.cargotracker.application.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.time.Instant;
import com.github.c5ms.cargotracker.application.command.CargoAssignRouteCommand;
import com.github.c5ms.cargotracker.application.command.CargoDestinationChangeCommand;
import com.github.c5ms.cargotracker.application.command.CargoRegisterCommand;
import com.github.c5ms.cargotracker.domain.model.cargo.Cargo;
import com.github.c5ms.cargotracker.domain.model.cargo.CargoFinder;
import com.github.c5ms.cargotracker.domain.model.cargo.CargoRepository;
import com.github.c5ms.cargotracker.domain.model.cargo.Itinerary;
import com.github.c5ms.cargotracker.domain.model.cargo.RouteSpecification;
import com.github.c5ms.cargotracker.domain.model.cargo.TrackingId;
import com.github.c5ms.cargotracker.domain.model.cargo.TrackingIdGenerator;
import com.github.c5ms.cargotracker.domain.model.location.LocationFinder;
import com.github.c5ms.cargotracker.domain.model.location.UnLocode;
import com.github.c5ms.cargotracker.sample.SampleLocations;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(
		classes = DefaultBookingService.class
)
class DefaultBookingServiceTest {

	@Autowired
	DefaultBookingService bookingService;

	@MockitoBean
	CargoRepository cargoRepository;

	@MockitoBean
	CargoFinder cargoFinder;

	@MockitoBean
	LocationFinder locationFinder;

	@MockitoBean
	TrackingIdGenerator trackingIdGenerator;

	@Test
	public void book() {
		var trackingId = TrackingId.of("001");
		var command = CargoRegisterCommand.builder()
				.arrivalDeadline(Instant.now())
				.destination(UnLocode.of(SampleLocations.STOCKHOLM.getUnlocode().getCode()))
				.origin(UnLocode.of(SampleLocations.SHANGHAI.getUnlocode().getCode()))
				.build();

		given(locationFinder.require(command.getOrigin())).willReturn(SampleLocations.STOCKHOLM);
		given(locationFinder.require(command.getDestination())).willReturn(SampleLocations.SHANGHAI);
		given(trackingIdGenerator.next()).willReturn(trackingId);

		bookingService.book(command);

		then(trackingIdGenerator).should(times(1)).next();
		then(cargoRepository).should(times(1)).save(Mockito.assertArg(cargo -> {
			assertThat(cargo).isNotNull();
			assertThat(cargo.getTrackingId()).isEqualTo(trackingId);
			assertThat(cargo.getRouteSpecification()).extracting(RouteSpecification::getOrigin).isEqualTo(SampleLocations.STOCKHOLM);
			assertThat(cargo.getRouteSpecification()).extracting(RouteSpecification::getDestination).isEqualTo(SampleLocations.SHANGHAI);
			assertThat(cargo.getRouteSpecification()).extracting(RouteSpecification::getArrivalDeadline).isEqualTo(command.getArrivalDeadline());
		}));
	}

	@Test
	public void assignRoute() {
		var trackingId = TrackingId.of("001");
		var cargo = mock(Cargo.class);
		var itinerary = mock(Itinerary.class);
		var command = CargoAssignRouteCommand.builder()
				.itinerary(itinerary)
				.build();

		given(cargoFinder.require(trackingId)).willReturn(cargo);

		bookingService.assignRoute(trackingId, command);

		then(cargo).should(times(1)).assignToRoute(command.getItinerary());
		then(cargoRepository).should(times(1)).save(cargo);
	}

	@Test
	public void changeDestination() {
		var changeLocation = SampleLocations.DALLAS;
		var cargo = mock(Cargo.class);
		var trackingId = TrackingId.of("001");
		var command = CargoDestinationChangeCommand.builder()
				.destination(UnLocode.of(changeLocation.getUnlocode().getCode()))
				.build();

		given(cargoFinder.require(trackingId)).willReturn(cargo);
		given(locationFinder.require(command.getDestination())).willReturn(changeLocation);

		bookingService.changeDestination(trackingId, command);

		then(cargo).should(times(1)).changeDestination(changeLocation);
		then(cargoRepository).should(times(1)).save(cargo);
	}


}