package se.citerus.dddsample.application.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.task.TaskExecutor;
import se.citerus.dddsample.application.command.HandlingReportProcessCommand;
import se.citerus.dddsample.application.command.HandlingReportReceiveCommand;
import se.citerus.dddsample.application.service.HandlingReportProcessor;
import se.citerus.dddsample.application.service.HandlingReportReceiver;
import se.citerus.dddsample.domain.model.handling.HandlingReport;

@Slf4j
@RequiredArgsConstructor
public class ThreadPooledHandlingReportReceiver implements HandlingReportReceiver {

    private final TaskExecutor taskExecutor;
    private final HandlingReportProcessor processor;

    @Override
    public void receiveHandlingReport(HandlingReportReceiveCommand command) {
        for (HandlingReport report : command.getReports()) {
            // using spring event publisher to publish command as event in a taskExecutor
            // so the command wil be handled by DefaultHandlingEventProcessor async.
            // todo add fail back handler
            taskExecutor.execute(() -> processInternal(report));
        }
    }

    private void processInternal(HandlingReport report) {
        try {
            var processCommand = HandlingReportProcessCommand.builder().report(report).build();
            processor.processHandingEvent(processCommand);
        } catch (Exception e) {
            log.error("report process fail", e);
        }
    }

}
