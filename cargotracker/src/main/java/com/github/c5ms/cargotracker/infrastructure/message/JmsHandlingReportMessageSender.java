package com.github.c5ms.cargotracker.infrastructure.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import com.github.c5ms.cargotracker.application.service.impl.HandlingReportMessageSender;
import com.github.c5ms.cargotracker.domain.model.handling.HandlingReport;

@Slf4j
@Component
@RequiredArgsConstructor
public class JmsHandlingReportMessageSender implements HandlingReportMessageSender {
    private final JmsTemplate jmsTemplate;

    @Override
    public void send(HandlingReport report) {
        jmsTemplate.send(JmsMessageQueues.QUEUE_NAME_HANDLING_REPORT_QUEUE, session -> session.createObjectMessage(report));
    }

}
