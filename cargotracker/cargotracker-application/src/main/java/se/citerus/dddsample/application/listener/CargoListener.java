package se.citerus.dddsample.application.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import se.citerus.dddsample.application.events.CargoArrivedEvent;
import se.citerus.dddsample.application.events.CargoHandledEvent;
import se.citerus.dddsample.application.events.CargoMisdirectedEvent;
import se.citerus.dddsample.application.service.CargoService;
import se.citerus.dddsample.application.shared.ApplicationEvent;
import se.citerus.dddsample.domain.model.cargo.TrackingId;
import se.citerus.dddsample.domain.shared.DomainEvent;

/**
 * event driven for cargo domain
 * note: this is not using for application event (application integration)
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CargoListener {

    private final CargoService cargoService;

    @EventListener(classes = DomainEvent.class)
    public void on(DomainEvent event) {
        log.info("Received domain event: {}", event);
    }

    @EventListener(classes = CargoHandledEvent.class)
    public void on(CargoHandledEvent event) {
        cargoService.inspectCargo(TrackingId.of(event.getTrackingId()));
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
