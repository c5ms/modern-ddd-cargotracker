package com.github.c5ms.cargotracker.application.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.github.c5ms.cargotracker.application.command.HandlingReportReceiveCommand;
import com.github.c5ms.cargotracker.application.service.HandlingReportReceiver;
import com.github.c5ms.cargotracker.domain.model.handling.HandlingReport;

@Slf4j
@RequiredArgsConstructor
public class MessageHandlingReportReceiver implements HandlingReportReceiver {
    private final HandlingReportMessageSender handlingReportMessageSender;

    @Override
    public void receiveHandlingReport(HandlingReportReceiveCommand command) {
        for (HandlingReport report : command.getReports()) {
            handlingReportMessageSender.send(report);
        }
    }

}
