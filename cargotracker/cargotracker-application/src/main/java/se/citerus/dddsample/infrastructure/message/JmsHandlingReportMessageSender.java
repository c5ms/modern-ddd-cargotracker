package se.citerus.dddsample.infrastructure.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import se.citerus.dddsample.application.service.impl.HandlingReportMessageSender;
import se.citerus.dddsample.domain.model.handling.HandlingReport;

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
