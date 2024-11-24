package se.citerus.dddsample.application.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import se.citerus.dddsample.application.shared.ApplicationEvent;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnMissingBean(ApplicationEventMessageSender.class)
public class InternalApplicationEventMessageSender implements ApplicationEventMessageSender {

    private final  ApplicationEventPublisher publisher;

    @Override
    public void send(ApplicationEvent event) {
        publisher.publishEvent(event);
    }
}
