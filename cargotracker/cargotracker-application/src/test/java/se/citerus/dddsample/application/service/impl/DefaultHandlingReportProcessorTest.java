package se.citerus.dddsample.application.service.impl;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import se.citerus.dddsample.application.command.HandlingReportProcessCommand;
import se.citerus.dddsample.application.events.CargoHandledEvent;
import se.citerus.dddsample.domain.model.cargo.TrackingId;
import se.citerus.dddsample.domain.model.handling.HandlingEvent;
import se.citerus.dddsample.domain.model.handling.HandlingEventFactory;
import se.citerus.dddsample.domain.model.handling.HandlingEventRepository;
import se.citerus.dddsample.domain.model.handling.HandlingReport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@SpringBootTest
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