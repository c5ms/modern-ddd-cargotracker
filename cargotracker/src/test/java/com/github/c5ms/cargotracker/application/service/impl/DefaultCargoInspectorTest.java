package com.github.c5ms.cargotracker.application.service.impl;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import com.github.c5ms.cargotracker.application.events.CargoArrivedEvent;
import com.github.c5ms.cargotracker.application.events.CargoMisdirectedEvent;
import com.github.c5ms.cargotracker.domain.model.cargo.Cargo;
import com.github.c5ms.cargotracker.domain.model.cargo.CargoFinder;
import com.github.c5ms.cargotracker.domain.model.cargo.CargoRepository;
import com.github.c5ms.cargotracker.domain.model.cargo.Delivery;
import com.github.c5ms.cargotracker.domain.model.cargo.TrackingId;
import com.github.c5ms.cargotracker.domain.model.handling.HandlingEventFinder;
import com.github.c5ms.cargotracker.domain.model.handling.HandlingHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

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