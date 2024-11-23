package se.citerus.dddsample.application.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.citerus.dddsample.application.service.BookingService;
import se.citerus.dddsample.application.command.CargoAssignRouteCommand;
import se.citerus.dddsample.application.command.CargoChangeDestinationCommand;
import se.citerus.dddsample.application.command.CargoRegisterCommand;
import se.citerus.dddsample.domain.model.cargo.*;
import se.citerus.dddsample.domain.model.location.Location;
import se.citerus.dddsample.domain.model.location.LocationFinder;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final CargoFinder cargoFinder;
    private final LocationFinder locationFinder;
    private final CargoRepository cargoRepository;
    private final TrackingIdGenerator trackingIdGenerator;

    @Override
    public Cargo book(CargoRegisterCommand command) {
        var cargo = Cargo.of(
            trackingIdGenerator.next(),
            locationFinder.require(command.getOrigin()),
            locationFinder.require(command.getDestination()),
            command.getArrivalDeadline());
        cargoRepository.save(cargo);
        return cargo;
    }

    @Override
    public void assignRoute(TrackingId trackingId, CargoAssignRouteCommand command) {
        final Cargo cargo = cargoFinder.require(trackingId);
        cargo.assignToRoute(command.getItinerary());
        cargoRepository.save(cargo);
    }

    @Override
    public void changeDestination(TrackingId trackingId, CargoChangeDestinationCommand command) {
        final Cargo cargo = cargoFinder.require(trackingId);
        final Location destination = locationFinder.require(command.getDestination());
        cargo.changeDestination(destination);
        cargoRepository.save(cargo);
    }

}
