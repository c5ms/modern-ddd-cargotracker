package com.github.c5ms.cargotracker.application.service.impl;

import com.github.c5ms.cargotracker.domain.model.handling.HandlingReport;

/**
 * Send HandlingReport  to outside infrastructure
 * <p>
 * e.g. HandlingReport-> rabbitMQ; HandlingReport-> activeMQ; HandlingReport-> localFile
 */
public interface HandlingReportMessageSender {
    void send(HandlingReport report);
}
