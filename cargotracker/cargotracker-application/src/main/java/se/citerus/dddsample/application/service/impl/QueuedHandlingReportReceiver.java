package se.citerus.dddsample.application.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import se.citerus.dddsample.application.command.HandlingReportReceiveCommand;
import se.citerus.dddsample.application.service.HandlingReportReceiver;
import se.citerus.dddsample.domain.model.handling.HandlingReport;

@Slf4j
@RequiredArgsConstructor
public class QueuedHandlingReportReceiver implements HandlingReportReceiver {
    private final HandlingReportMessageSender handlingReportMessageSender;

    @Override
    public void receiveHandlingReport(HandlingReportReceiveCommand command) {
        for (HandlingReport report : command.getReports()) {
            handlingReportMessageSender.send(report);
        }
    }

}
