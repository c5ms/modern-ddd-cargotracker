package se.citerus.dddsample.application.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import se.citerus.dddsample.application.events.CargoArrivedEvent;
import se.citerus.dddsample.application.events.CargoMisdirectedEvent;
import se.citerus.dddsample.domain.model.cargo.*;
import se.citerus.dddsample.domain.model.handling.HandlingEventFinder;
import se.citerus.dddsample.domain.model.handling.HandlingHistory;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@SpringBootTest(
    classes = DefaultCargoInspector.class
)
class DefaultCargoInspectorTest {
    @MockitoBean
    CargoFinder cargoFinder;

    @MockitoBean
    CargoRepository cargoRepository;

    @MockitoBean
    HandlingEventFinder handlingEventFinder;

    @MockitoBean
    ApplicationEventMessageSender applicationEventMessageSender;

    @Autowired
    DefaultCargoInspector cargoInspector;

    private TrackingId trackingId;
    private Cargo cargo;
    private Delivery delivery;
    private HandlingHistory handlingHistory;

    @BeforeEach
    void setUp() {
        trackingId = TrackingId.of("001");
        cargo = mock(Cargo.class);
        delivery = mock(Delivery.class);
        handlingHistory = mock(HandlingHistory.class);

        given(cargo.getTrackingId()).willReturn(trackingId);
        given(cargo.getDelivery()).willReturn(delivery);
        given(cargoFinder.require(Mockito.eq(trackingId))).willReturn(cargo);
        given(handlingEventFinder.handlingHistoryOf(Mockito.eq(trackingId))).willReturn(handlingHistory);
    }

    @Test
    void inspectCargo_misdirected() {
        given(delivery.isMisdirected()).willReturn(true);
        cargoInspector.inspectCargo(trackingId);
        then(applicationEventMessageSender).should(times(1)).send(CargoMisdirectedEvent.of(trackingId.getId()));

        then(cargo).should(times(1)).deriveDeliveryProgress(handlingHistory);
        then(cargoRepository).should(times(1)).save(cargo);
    }


    @Test
    void inspectCargo_unloadedAtDestination() {
        given(delivery.isUnloadedAtDestination()).willReturn(true);
        cargoInspector.inspectCargo(trackingId);
        then(applicationEventMessageSender).should(times(1)).send(CargoArrivedEvent.of(trackingId.getId()));

        then(cargo).should(times(1)).deriveDeliveryProgress(handlingHistory);
        then(cargoRepository).should(times(1)).save(cargo);
    }

}