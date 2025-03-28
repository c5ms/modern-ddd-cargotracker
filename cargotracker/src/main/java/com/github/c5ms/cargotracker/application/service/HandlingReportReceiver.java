package com.github.c5ms.cargotracker.application.service;

import com.github.c5ms.cargotracker.application.command.HandlingReportReceiveCommand;

/**
 * This class can not guarantee the command be processed immediately
 * if you want the event be processed immediately please refer to {@link HandlingReportProcessor}
 */
public interface HandlingReportReceiver {

    void receiveHandlingReport(HandlingReportReceiveCommand command);

}
