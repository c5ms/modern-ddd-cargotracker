package se.citerus.dddsample.application.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import se.citerus.dddsample.application.command.HandlingReportProcessCommand;
import se.citerus.dddsample.application.events.CargoHandledEvent;
import se.citerus.dddsample.application.service.HandlingReportProcessor;
import se.citerus.dddsample.domain.model.handling.HandlingEvent;
import se.citerus.dddsample.domain.model.handling.HandlingEventFactory;
import se.citerus.dddsample.domain.model.handling.HandlingEventRepository;

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
