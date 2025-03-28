package com.github.c5ms.cargotracker.application.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import com.github.c5ms.cargotracker.application.command.HandlingReportProcessCommand;
import com.github.c5ms.cargotracker.application.events.CargoHandledEvent;
import com.github.c5ms.cargotracker.application.service.HandlingReportProcessor;
import com.github.c5ms.cargotracker.domain.model.handling.HandlingEvent;
import com.github.c5ms.cargotracker.domain.model.handling.HandlingEventFactory;
import com.github.c5ms.cargotracker.domain.model.handling.HandlingEventRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultHandlingReportProcessor implements HandlingReportProcessor {

    private final HandlingEventFactory handlingEventFactory;
    private final ApplicationEventMessageSender applicationEventMessageSender;
    private final HandlingEventRepository handlingEventRepository;

    @Override
    @EventListener(HandlingReportProcessCommand.class)
    public void processHandingEvent(HandlingReportProcessCommand command) {
        log.info("Handling report processing command: {}", command);
        var report = command.getReport();
        final HandlingEvent event = handlingEventFactory.createHandlingEvent(report);
        handlingEventRepository.save(event);
        applicationEventMessageSender.send(CargoHandledEvent.of(report.getTrackingId().getId()));
    }

}
