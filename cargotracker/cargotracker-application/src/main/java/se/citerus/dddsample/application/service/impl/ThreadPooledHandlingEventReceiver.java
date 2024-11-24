package se.citerus.dddsample.application.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.task.TaskExecutor;
import se.citerus.dddsample.application.command.HandlingReportProcessCommand;
import se.citerus.dddsample.application.command.HandlingReportReceiveCommand;
import se.citerus.dddsample.application.service.HandlingEventReceiver;
import se.citerus.dddsample.domain.model.handling.HandlingEventReport;

@Slf4j
@RequiredArgsConstructor
public class ThreadPooledHandlingEventReceiver implements HandlingEventReceiver {

    private final TaskExecutor taskExecutor;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void receiveHandlingReport(HandlingReportReceiveCommand command) {
        for (HandlingEventReport report : command.getReports()) {
            // using spring event publisher to publish command as event in a taskExecutor
            // so the command wil be handled by DefaultHandlingEventProcessor async.
            // todo add fail back handler
            taskExecutor.execute(() -> processInternal(report));
        }
    }

    private void processInternal(HandlingEventReport report) {
        try {
            var processCommand = HandlingReportProcessCommand.builder().report(report).build();
            eventPublisher.publishEvent(processCommand);
        } catch (Exception e) {
            log.error("report process fail", e);
        }
    }

}
