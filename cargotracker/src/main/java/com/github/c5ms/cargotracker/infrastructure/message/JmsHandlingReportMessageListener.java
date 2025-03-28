package com.github.c5ms.cargotracker.infrastructure.message;

import jakarta.jms.Message;
import jakarta.jms.ObjectMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import com.github.c5ms.cargotracker.application.command.HandlingReportProcessCommand;
import com.github.c5ms.cargotracker.application.service.HandlingReportProcessor;
import com.github.c5ms.cargotracker.domain.model.handling.HandlingReport;

@Slf4j
@Component
@RequiredArgsConstructor
public class JmsHandlingReportMessageListener {
    private final HandlingReportProcessor handlingReportProcessor;

    @JmsListener(destination = JmsMessageQueues.QUEUE_NAME_HANDLING_REPORT_QUEUE)
    public void onHandleReport(Message message) {
        try {
            var om = (ObjectMessage) message;
            var report = (HandlingReport) om.getObject();
            var command = HandlingReportProcessCommand.builder().report(report).build();
            handlingReportProcessor.processHandingEvent(command);
        } catch (Exception e) {
            log.error("HandleReport message handle fail", e);
        }
    }

}
