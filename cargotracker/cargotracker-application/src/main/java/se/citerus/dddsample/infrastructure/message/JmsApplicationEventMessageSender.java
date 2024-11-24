package se.citerus.dddsample.infrastructure.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import se.citerus.dddsample.application.events.CargoArrivedEvent;
import se.citerus.dddsample.application.events.CargoHandledEvent;
import se.citerus.dddsample.application.events.CargoMisdirectedEvent;
import se.citerus.dddsample.application.service.impl.ApplicationEventMessageSender;
import se.citerus.dddsample.application.shared.ApplicationEvent;

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

        log.error("unsupported event type: {}",  event.getClass().getName());
    }

}
