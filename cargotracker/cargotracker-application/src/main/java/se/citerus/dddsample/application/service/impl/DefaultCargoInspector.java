package se.citerus.dddsample.application.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.citerus.dddsample.application.events.CargoArrivedEvent;
import se.citerus.dddsample.application.events.CargoMisdirectedEvent;
import se.citerus.dddsample.application.service.CargoInspector;
import se.citerus.dddsample.domain.model.cargo.Cargo;
import se.citerus.dddsample.domain.model.cargo.CargoFinder;
import se.citerus.dddsample.domain.model.cargo.CargoRepository;
import se.citerus.dddsample.domain.model.cargo.TrackingId;
import se.citerus.dddsample.domain.model.handling.HandlingEventFinder;
import se.citerus.dddsample.domain.model.handling.HandlingHistory;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DefaultCargoInspector implements CargoInspector {

    private final CargoFinder cargoFinder;
    private final CargoRepository cargoRepository;
    private final HandlingEventFinder handlingEventFinder;
    private final ApplicationEventMessageSender applicationEventMessageSender;

    @Override
    public void inspectCargo(TrackingId trackingId) {
        Validate.notNull(trackingId, "Tracking ID is required");

        final Cargo cargo = cargoFinder.require(trackingId);
        final HandlingHistory handlingHistory = handlingEventFinder.handlingHistoryOf(trackingId);

        cargo.deriveDeliveryProgress(handlingHistory);

        if (cargo.getDelivery().isMisdirected()) {
            applicationEventMessageSender.send(CargoMisdirectedEvent.of(trackingId.getId()));
        }

        if (cargo.getDelivery().isUnloadedAtDestination()) {
            applicationEventMessageSender.send(CargoArrivedEvent.of(trackingId.getId()));
        }

        cargoRepository.save(cargo);
    }
}
