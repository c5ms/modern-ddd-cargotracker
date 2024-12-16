package se.citerus.dddsample.application.service.impl;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import se.citerus.dddsample.application.command.CargoAssignRouteCommand;
import se.citerus.dddsample.application.command.CargoDestinationChangeCommand;
import se.citerus.dddsample.application.command.CargoRegisterCommand;
import se.citerus.dddsample.domain.model.cargo.*;
import se.citerus.dddsample.domain.model.location.LocationFinder;
import se.citerus.dddsample.domain.model.location.UnLocode;
import se.citerus.dddsample.sample.SampleLocations;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

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