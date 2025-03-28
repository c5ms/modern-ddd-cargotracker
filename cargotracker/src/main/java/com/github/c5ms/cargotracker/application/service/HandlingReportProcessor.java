package com.github.c5ms.cargotracker.application.service;

import com.github.c5ms.cargotracker.application.command.HandlingReportProcessCommand;


/**
 * Handling event service.
 */
public interface HandlingReportProcessor {

    void processHandingEvent(HandlingReportProcessCommand command);

}
