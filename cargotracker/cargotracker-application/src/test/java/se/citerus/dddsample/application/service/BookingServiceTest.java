package se.citerus.dddsample.application.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import se.citerus.dddsample.application.command.CargoAssignRouteCommand;
import se.citerus.dddsample.application.command.CargoDestinationChangeCommand;
import se.citerus.dddsample.application.command.CargoRegisterCommand;
import se.citerus.dddsample.application.service.impl.DefaultBookingService;
import se.citerus.dddsample.domain.model.cargo.*;
import se.citerus.dddsample.domain.model.location.UnLocode;
import se.citerus.dddsample.infrastructure.initialize.SampleLocations;
import se.citerus.dddsample.infrastructure.initialize.SampleVoyages;
import se.citerus.dddsample.utils.TestCargoGenerator;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@SpringBootTest
class BookingServiceTest {

    @Autowired
    DefaultBookingService bookingService;

    @MockitoBean
    CargoRepository cargoRepository;

    @MockitoBean
    CargoFinder cargoFinder;

    @Test
    public void book() {
        var command = CargoRegisterCommand.builder()
            .arrivalDeadline(Instant.now())
            .destination(UnLocode.of(SampleLocations.STOCKHOLM.getUnlocode()))
            .origin(UnLocode.of(SampleLocations.SHANGHAI.getUnlocode()))
            .build();
        var cargo = bookingService.book(command);
        then(cargoRepository).should(Mockito.times(1)).save(Mockito.any(Cargo.class));

        Assertions.assertNotNull(cargo);
        Assertions.assertNotNull(cargo.getDelivery());
        Assertions.assertNotNull(cargo.getLegs());
        Assertions.assertTrue(cargo.getLegs().isEmpty());
    }

    @Test
    public void assignRoute() {
        var trackingId = TrackingId.of("001");
        var command = CargoAssignRouteCommand.builder()
            .itinerary(Itinerary.of(
                List.of(
                    Leg.of(SampleVoyages.CM005,
                        TestCargoGenerator.fromLocation(),
                        TestCargoGenerator.toLocation(),
                        Instant.now(),
                        Instant.now().plus(1, ChronoUnit.DAYS))
                )))
            .build();

        given(cargoFinder.require(trackingId)).willReturn(TestCargoGenerator.emptyCargo());
        bookingService.assignRoute(trackingId, command);
        then(cargoRepository).should(Mockito.times(1)).save(Mockito.any(Cargo.class));
    }

    @Test
    public void changeDestination() {
        var trackingId = TrackingId.of("001");
        var command = CargoDestinationChangeCommand.builder()
            .destination(UnLocode.of(TestCargoGenerator.changeLocation().getUnlocode()))
            .build();
        given(cargoFinder.require(trackingId)).willReturn(TestCargoGenerator.emptyCargo());
        bookingService.changeDestination(trackingId, command);
        then(cargoRepository).should(Mockito.times(1)).save(Mockito.any(Cargo.class));
    }



}