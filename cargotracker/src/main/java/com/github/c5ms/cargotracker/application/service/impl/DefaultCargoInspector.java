package com.github.c5ms.cargotracker.application.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.c5ms.cargotracker.application.events.CargoArrivedEvent;
import com.github.c5ms.cargotracker.application.events.CargoMisdirectedEvent;
import com.github.c5ms.cargotracker.application.service.CargoInspector;
import com.github.c5ms.cargotracker.domain.model.cargo.Cargo;
import com.github.c5ms.cargotracker.domain.model.cargo.CargoFinder;
import com.github.c5ms.cargotracker.domain.model.cargo.CargoRepository;
import com.github.c5ms.cargotracker.domain.model.cargo.TrackingId;
import com.github.c5ms.cargotracker.domain.model.handling.HandlingEventFinder;
import com.github.c5ms.cargotracker.domain.model.handling.HandlingHistory;

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
