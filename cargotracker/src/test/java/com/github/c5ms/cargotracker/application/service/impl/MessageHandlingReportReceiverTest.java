package com.github.c5ms.cargotracker.application.service.impl;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import java.util.List;
import com.github.c5ms.cargotracker.application.command.HandlingReportReceiveCommand;
import com.github.c5ms.cargotracker.application.configure.CargoTrackerApplicationConfigure;
import com.github.c5ms.cargotracker.application.service.HandlingReportProcessor;
import com.github.c5ms.cargotracker.application.service.HandlingReportReceiver;
import com.github.c5ms.cargotracker.domain.model.handling.HandlingReport;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

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