package se.citerus.dddsample.application.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.citerus.dddsample.application.events.CargoArrivedEvent;
import se.citerus.dddsample.application.events.CargoMisdirectedEvent;
import se.citerus.dddsample.application.service.CargoService;
import se.citerus.dddsample.application.command.CargoAssignRouteCommand;
import se.citerus.dddsample.application.command.CargoChangeDestinationCommand;
import se.citerus.dddsample.application.command.CargoRegisterCommand;
import se.citerus.dddsample.domain.model.cargo.*;
import se.citerus.dddsample.domain.model.handling.HandlingEventFinder;
import se.citerus.dddsample.domain.model.handling.HandlingEventRepository;
import se.citerus.dddsample.domain.model.handling.HandlingHistory;
import se.citerus.dddsample.domain.model.handling.support.DefaultHandlingEventFinder;
import se.citerus.dddsample.domain.model.location.Location;
import se.citerus.dddsample.domain.model.location.LocationFinder;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DefaultCargoService implements CargoService {

    /* read only */
    private final CargoFinder cargoFinder;
    private final LocationFinder locationFinder;
    private final HandlingEventFinder handlingEventFinder;

    /* writable */
    private final CargoRepository cargoRepository;

    /* domain service */
    private final TrackingIdGenerator trackingIdGenerator;

    /* inner support */
    private final ApplicationEventSender applicationEventSender;


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

    @Override
    public void inspectCargo(TrackingId trackingId) {
        Validate.notNull(trackingId, "Tracking ID is required");

        final Cargo cargo = cargoFinder.require(trackingId);
        final HandlingHistory handlingHistory = handlingEventFinder.handlingHistoryOf(trackingId);

        cargo.deriveDeliveryProgress(handlingHistory);

        if (cargo.getDelivery().isMisdirected()) {
            applicationEventSender.send(CargoMisdirectedEvent.of(trackingId.getId()));
        }

        if (cargo.getDelivery().isUnloadedAtDestination()) {
            applicationEventSender.send(CargoArrivedEvent.of(trackingId.getId()));
        }

        cargoRepository.save(cargo);
    }

}
