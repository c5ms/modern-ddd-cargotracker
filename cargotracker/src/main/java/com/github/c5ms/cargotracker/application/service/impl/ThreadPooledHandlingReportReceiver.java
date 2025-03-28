package com.github.c5ms.cargotracker.application.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;
import com.github.c5ms.cargotracker.application.command.HandlingReportProcessCommand;
import com.github.c5ms.cargotracker.application.command.HandlingReportReceiveCommand;
import com.github.c5ms.cargotracker.application.service.HandlingReportProcessor;
import com.github.c5ms.cargotracker.application.service.HandlingReportReceiver;
import com.github.c5ms.cargotracker.domain.model.handling.HandlingReport;

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
