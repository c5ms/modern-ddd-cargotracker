package se.citerus.dddsample.infrastructure.message;

import jakarta.jms.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import se.citerus.dddsample.application.events.CargoArrivedEvent;
import se.citerus.dddsample.application.events.CargoHandledEvent;
import se.citerus.dddsample.application.events.CargoMisdirectedEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class JmsApplicationEventMessageListener {

    private final ApplicationEventPublisher publisher;

    @JmsListener(destination = JmsMessageQueues.QUEUE_NAME_CARGO_HANDLED_QUEUE)
    public void onCargoHandled(Message message) {
        try {
            var trackingId = message.getBody(String.class);
            publisher.publishEvent(CargoHandledEvent.of(trackingId));
        } catch (Exception e) {
            log.error("CargoHandledEvent message handle fail", e);
        }
    }

    @JmsListener(destination = JmsMessageQueues.QUEUE_NAME_CARGO_ARRIVED_QUEUE)
    public void onCargoArrived(Message message) {
        try {
            var trackingId = message.getBody(String.class);
            publisher.publishEvent(CargoArrivedEvent.of(trackingId));
        } catch (Exception e) {
            log.error("CargoArrivedEvent message handle fail", e);
        }
    }

    @JmsListener(destination = JmsMessageQueues.QUEUE_NAME_CARGO_MISDIRECTED_QUEUE)
    public void onCargoMisdirected(Message message) {
        try {
            var trackingId = message.getBody(String.class);
            publisher.publishEvent(CargoMisdirectedEvent.of(trackingId));
        } catch (Exception e) {
            log.error("CargoMisdirectedEvent message handle fail", e);
        }
    }

}
