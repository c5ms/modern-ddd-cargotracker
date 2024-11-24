package se.citerus.dddsample.application.service;

import se.citerus.dddsample.application.command.HandlingReportProcessCommand;


/**
 * Handling event service.
 */
public interface HandlingEventProcessor {

    void processHandingEvent(HandlingReportProcessCommand command);

}
