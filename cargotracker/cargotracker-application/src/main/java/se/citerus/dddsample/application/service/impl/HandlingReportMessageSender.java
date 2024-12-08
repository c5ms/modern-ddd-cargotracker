package se.citerus.dddsample.application.service.impl;

import se.citerus.dddsample.domain.model.handling.HandlingReport;

/**
 * Send HandlingReport  to outside infrastructure
 * <p>
 * e.g. HandlingReport-> rabbitMQ; HandlingReport-> activeMQ; HandlingReport-> localFile
 */
public interface HandlingReportMessageSender {
    void send(HandlingReport report);
}
