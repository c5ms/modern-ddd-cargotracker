package com.github.c5ms.cargotracker.application.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import com.github.c5ms.cargotracker.application.shared.ApplicationEvent;

@Slf4j
@RequiredArgsConstructor
public class DirectlyApplicationEventMessageSender implements ApplicationEventMessageSender {

    private final ApplicationEventPublisher publisher;

    @Override
    public void send(ApplicationEvent event) {
        publisher.publishEvent(event);
    }
}
