package com.github.c5ms.cargotracker.application.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import com.github.c5ms.cargotracker.application.events.CargoArrivedEvent;
import com.github.c5ms.cargotracker.application.events.CargoHandledEvent;
import com.github.c5ms.cargotracker.application.events.CargoMisdirectedEvent;
import com.github.c5ms.cargotracker.application.service.CargoInspector;
import com.github.c5ms.cargotracker.domain.model.cargo.TrackingId;
import com.github.c5ms.cargotracker.domain.shared.DomainEvent;

/**
 * event driven for cargo domain
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CargoListener {

    private final CargoInspector cargoInspector;

    @EventListener(classes = DomainEvent.class)
    public void on(DomainEvent event) {
        log.info("Received domain event: {}", event);
    }

    @EventListener(classes = CargoHandledEvent.class)
    public void on(CargoHandledEvent event) {
        cargoInspector.inspectCargo(TrackingId.of(event.getTrackingId()));
    }

    @EventListener(classes = CargoMisdirectedEvent.class)
    public void on(CargoMisdirectedEvent event) {
        log.info("Received CargoMisdirectedEvent : {}", event);
    }

    @EventListener(classes = CargoArrivedEvent.class)
    public void on(CargoArrivedEvent event) {
        log.info("Received CargoArrivedEvent: {}", event);
    }

}
