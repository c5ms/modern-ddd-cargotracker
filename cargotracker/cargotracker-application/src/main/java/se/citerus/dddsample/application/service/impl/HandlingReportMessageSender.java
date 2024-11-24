package se.citerus.dddsample.application.service.impl;

import se.citerus.dddsample.domain.model.handling.HandlingReport;

public interface HandlingReportMessageSender {
    void send(HandlingReport event);
}
