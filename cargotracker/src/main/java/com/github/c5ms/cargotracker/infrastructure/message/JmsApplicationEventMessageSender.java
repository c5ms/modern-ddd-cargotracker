package com.github.c5ms.cargotracker.infrastructure.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import com.github.c5ms.cargotracker.application.events.CargoArrivedEvent;
import com.github.c5ms.cargotracker.application.events.CargoHandledEvent;
import com.github.c5ms.cargotracker.application.events.CargoMisdirectedEvent;
import com.github.c5ms.cargotracker.application.service.impl.ApplicationEventMessageSender;
import com.github.c5ms.cargotracker.application.shared.ApplicationEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class JmsApplicationEventMessageSender implements ApplicationEventMessageSender {

    private final JmsTemplate jmsTemplate;

    @Override
    public void send(ApplicationEvent event) {

        if (event instanceof CargoHandledEvent cargoHandledEvent) {
            jmsTemplate.convertAndSend(JmsMessageQueues.QUEUE_NAME_CARGO_HANDLED_QUEUE, cargoHandledEvent.getTrackingId());
            return;
        }

        if (event instanceof CargoArrivedEvent subEvent) {
            jmsTemplate.convertAndSend(JmsMessageQueues.QUEUE_NAME_CARGO_ARRIVED_QUEUE, subEvent.getTrackingId());
            return;
        }

        if (event instanceof CargoMisdirectedEvent subEvent) {
            jmsTemplate.convertAndSend(JmsMessageQueues.QUEUE_NAME_CARGO_MISDIRECTED_QUEUE, subEvent.getTrackingId());
            return;
        }

        log.error("unsupported event type: {}", event.getClass().getName());
    }

}
