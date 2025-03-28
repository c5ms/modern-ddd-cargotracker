package com.github.c5ms.cargotracker.interfaces.restful.resource;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.List;
import com.github.c5ms.cargotracker.domain.model.location.LocationFinder;
import com.github.c5ms.cargotracker.infrastructure.configure.CargoTrackerJacksonConfigure;
import com.github.c5ms.cargotracker.interfaces.configure.CargoTrackerInterfacesConfigure;
import com.github.c5ms.cargotracker.interfaces.model.convertor.CargoConvertor;
import com.github.c5ms.cargotracker.sample.SampleLocations;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@WebMvcTest(LocationResource.class)
@Import({
		CargoTrackerInterfacesConfigure.class,
		CargoTrackerJacksonConfigure.class
})
class LocationResourceTest {

	@Autowired
	MockMvcTester mvc;

	@MockitoBean
	LocationFinder locationFinder;

	@MockitoBean
	CargoConvertor cargoConvertor;

	@Test
	void list_shouldReturnOK() {
		given(locationFinder.listAll()).willReturn(List.of(SampleLocations.HAMBURG));

		assertThat(this.mvc.perform(get("/locations")))
				.hasStatusOk();
	}


}