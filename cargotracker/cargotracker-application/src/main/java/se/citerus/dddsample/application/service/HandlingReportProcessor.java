package se.citerus.dddsample.application.service;

import se.citerus.dddsample.application.command.HandlingReportProcessCommand;


/**
 * Handling event service.
 */
public interface HandlingReportProcessor {

    void processHandingEvent(HandlingReportProcessCommand command);

}
