package com.github.c5ms.cargotracker.application.service.impl;

import com.github.c5ms.cargotracker.domain.model.cargo.Cargo;
import com.github.c5ms.cargotracker.domain.model.cargo.CargoFinder;
import com.github.c5ms.cargotracker.domain.model.cargo.CargoRepository;
import com.github.c5ms.cargotracker.domain.model.cargo.TrackingId;
import com.github.c5ms.cargotracker.domain.model.cargo.TrackingIdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.c5ms.cargotracker.application.command.CargoAssignRouteCommand;
import com.github.c5ms.cargotracker.application.command.CargoDestinationChangeCommand;
import com.github.c5ms.cargotracker.application.command.CargoRegisterCommand;
import com.github.c5ms.cargotracker.application.service.BookingService;
import com.github.c5ms.cargotracker.domain.model.location.Location;
import com.github.c5ms.cargotracker.domain.model.location.LocationFinder;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DefaultBookingService implements BookingService {

    /* read only */
    private final CargoFinder cargoFinder;
    private final LocationFinder locationFinder;

    /* writable */
    private final CargoRepository cargoRepository;

    /* domain service */
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
    public void changeDestination(TrackingId trackingId, CargoDestinationChangeCommand command) {
        final Cargo cargo = cargoFinder.require(trackingId);
        final Location destination = locationFinder.require(command.getDestination());
        cargo.changeDestination(destination);
        cargoRepository.save(cargo);
    }

}
