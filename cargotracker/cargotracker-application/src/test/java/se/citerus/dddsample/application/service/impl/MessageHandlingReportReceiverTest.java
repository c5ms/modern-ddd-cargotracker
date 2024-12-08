package se.citerus.dddsample.application.service.impl;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import se.citerus.dddsample.application.command.HandlingReportReceiveCommand;
import se.citerus.dddsample.application.configure.CargoTrackerApplicationConfigure;
import se.citerus.dddsample.application.service.HandlingReportProcessor;
import se.citerus.dddsample.application.service.HandlingReportReceiver;
import se.citerus.dddsample.domain.model.handling.HandlingReport;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@SpringBootTest(
    classes = {
        CargoTrackerApplicationConfigure.class,
        HandlingReportProcessor.class
    },
    properties = {
        "cargotracker.application.handing-report.process-strategy=MESSAGE",
    })
class MessageHandlingReportReceiverTest {

    @MockitoBean
    HandlingReportMessageSender handlingReportMessageSender;

    @Autowired
    HandlingReportReceiver handlingReportReceiver;

    @Test
    void receiveHandlingReport() {
        var report1 = Mockito.mock(HandlingReport.class);
        var report2 = Mockito.mock(HandlingReport.class);
        var command = HandlingReportReceiveCommand.builder()
            .reports(List.of(report1, report2))
            .build();
        handlingReportReceiver.receiveHandlingReport(command);
        then(handlingReportMessageSender).should(times(1)).send(eq(report1));
        then(handlingReportMessageSender).should(times(1)).send(eq(report2));
    }
}