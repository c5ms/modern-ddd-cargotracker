package com.github.c5ms.cargotracker.interfaces.model.convertor;

import java.time.LocalDate;
import java.time.ZoneOffset;
import com.github.c5ms.cargotracker.domain.model.location.LocationFinder;
import com.github.c5ms.cargotracker.domain.model.voyage.VoyageFinder;
import com.github.c5ms.cargotracker.interfaces.model.request.CargoRegisterRequest;
import com.github.c5ms.cargotracker.sample.SampleLocations;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(classes = CargoConvertor.class)
class CargoConvertorTest {

	@MockitoBean
	VoyageFinder voyageFinder;

	@MockitoBean
	LocationFinder locationFinder;

	@InjectMocks
	CargoConvertor cargoConvertor;

	@Test
	void toCargoRegisterCommand() {
		var request = new CargoRegisterRequest()
				.setArrivalDeadline(LocalDate.now().plusDays(1))
				.setOriginUnlocode(SampleLocations.HAMBURG.getUnlocode().getCode())
				.setDestinationUnlocode(SampleLocations.STOCKHOLM.getUnlocode().getCode());
		var command = cargoConvertor.toCommand(request);

		Assertions.assertNotNull(command);
		Assertions.assertEquals(LocalDate.now().plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC), command.getArrivalDeadline());
		Assertions.assertEquals(SampleLocations.HAMBURG.getUnlocode(), command.getOrigin());
		Assertions.assertEquals(SampleLocations.STOCKHOLM.getUnlocode(), command.getDestination());
	}

}

