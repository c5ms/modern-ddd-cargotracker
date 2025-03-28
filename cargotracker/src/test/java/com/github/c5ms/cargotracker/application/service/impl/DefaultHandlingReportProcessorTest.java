package com.github.c5ms.cargotracker.application.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.github.c5ms.cargotracker.application.command.HandlingReportProcessCommand;
import com.github.c5ms.cargotracker.application.events.CargoHandledEvent;
import com.github.c5ms.cargotracker.domain.model.cargo.TrackingId;
import com.github.c5ms.cargotracker.domain.model.handling.HandlingEvent;
import com.github.c5ms.cargotracker.domain.model.handling.HandlingEventFactory;
import com.github.c5ms.cargotracker.domain.model.handling.HandlingEventRepository;
import com.github.c5ms.cargotracker.domain.model.handling.HandlingReport;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(
		classes = DefaultHandlingReportProcessor.class
)
class DefaultHandlingReportProcessorTest {

	@Autowired
	DefaultHandlingReportProcessor handlingReportProcessor;

	@MockitoBean
	ApplicationEventMessageSender applicationEventMessageSender;

	@MockitoBean
	HandlingEventFactory handlingEventFactory;

	@MockitoBean
	HandlingEventRepository handlingEventRepository;

	@Test
	void processHandingEvent() {
		var report = Mockito.mock(HandlingReport.class);
		var event = Mockito.mock(HandlingEvent.class);

		given(report.getTrackingId()).willReturn(TrackingId.of("001"));
		given(handlingEventFactory.createHandlingEvent(report)).willReturn(event);

		handlingReportProcessor.processHandingEvent(
				HandlingReportProcessCommand.builder()
						.report(report)
						.build()
		);

		then(applicationEventMessageSender).should(times(1)).send(Mockito.assertArg(applicationEvent -> {
			assertThat(applicationEvent).isEqualTo(CargoHandledEvent.of("001"));
		}));
		then(handlingEventRepository).should(times(1)).save(event);

	}
}