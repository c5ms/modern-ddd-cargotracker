package se.citerus.dddsample.application.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import se.citerus.dddsample.application.command.HandlingReportProcessCommand;
import se.citerus.dddsample.application.command.HandlingReportReceiveCommand;
import se.citerus.dddsample.application.service.HandlingReportProcessor;
import se.citerus.dddsample.application.service.HandlingReportHandler;
import se.citerus.dddsample.domain.model.handling.HandlingReport;

@Slf4j
@RequiredArgsConstructor
public class DirectlyHandlingReportHandler implements HandlingReportHandler {
    private final HandlingReportProcessor handlingReportProcessor;

    @Override
    public void receiveHandlingReport(HandlingReportReceiveCommand command) {
        for (HandlingReport report : command.getReports()) {
            var processCommand = HandlingReportProcessCommand.builder().report(report).build();
            handlingReportProcessor.processHandingEvent(processCommand);
        }
    }

}
