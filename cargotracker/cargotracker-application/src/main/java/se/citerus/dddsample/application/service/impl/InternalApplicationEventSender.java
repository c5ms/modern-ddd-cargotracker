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
@ConditionalOnMissingBean(ApplicationEventSender.class)
public class InternalApplicationEventSender implements ApplicationEventSender{

    private final  ApplicationEventPublisher publisher;

    @Override
    public void send(ApplicationEvent event) {
        publisher.publishEvent(event);
    }
}
