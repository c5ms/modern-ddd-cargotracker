package com.github.c5ms.cargotracker.application.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.github.c5ms.cargotracker.application.command.HandlingReportProcessCommand;
import com.github.c5ms.cargotracker.application.command.HandlingReportReceiveCommand;
import com.github.c5ms.cargotracker.application.service.HandlingReportProcessor;
import com.github.c5ms.cargotracker.application.service.HandlingReportReceiver;
import com.github.c5ms.cargotracker.domain.model.handling.HandlingReport;

@Slf4j
@RequiredArgsConstructor
public class DirectlyHandlingReportReceiver implements HandlingReportReceiver {
    private final HandlingReportProcessor handlingReportProcessor;

    @Override
    public void receiveHandlingReport(HandlingReportReceiveCommand command) {
        for (HandlingReport report : command.getReports()) {
            var processCommand = HandlingReportProcessCommand.builder().report(report).build();
            handlingReportProcessor.processHandingEvent(processCommand);
        }
    }

}
