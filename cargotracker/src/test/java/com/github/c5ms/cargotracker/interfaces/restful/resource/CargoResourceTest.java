package com.github.c5ms.cargotracker.interfaces.restful.resource;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.time.Instant;
import java.util.List;
import com.github.c5ms.cargotracker.application.service.BookingService;
import com.github.c5ms.cargotracker.domain.model.cargo.Cargo;
import com.github.c5ms.cargotracker.domain.model.cargo.CargoFinder;
import com.github.c5ms.cargotracker.domain.model.cargo.TrackingId;
import com.github.c5ms.cargotracker.domain.model.cargo.UnknownCargoException;
import com.github.c5ms.cargotracker.infrastructure.configure.CargoTrackerJacksonConfigure;
import com.github.c5ms.cargotracker.interfaces.configure.CargoTrackerInterfacesConfigure;
import com.github.c5ms.cargotracker.interfaces.model.convertor.CargoConvertor;
import com.github.c5ms.cargotracker.sample.SampleLocations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@WebMvcTest(CargoResource.class)
@Import({
		CargoTrackerInterfacesConfigure.class,
		CargoTrackerJacksonConfigure.class
})
class CargoResourceTest {

	@Autowired
	MockMvcTester mvc;

	@MockitoBean
	CargoFinder cargoFinder;

	@MockitoBean
	BookingService bookingService;

	@MockitoBean
	CargoConvertor cargoConvertor;

	Cargo cargo;

	@BeforeEach
	void setUp() {
		cargo = Cargo.of(TrackingId.of("T001"), SampleLocations.STOCKHOLM, SampleLocations.TOKYO, Instant.now());
	}

	@Test
	void list_shouldReturnOK() {
		when(cargoFinder.listAll()).thenReturn(List.of(cargo));
		assertThat(this.mvc.perform(get("/cargos"))).hasStatusOk();
	}

	@Test
	void read_shouldReturnOK() {
		when(cargoFinder.require(any())).thenThrow(new UnknownCargoException(TrackingId.of("001")));
		assertThat(this.mvc.get().uri("/cargos/{trackingId}", "001"))
				.hasStatus(HttpStatus.NOT_FOUND);
	}

	@Test
	void read_shouldReturnNotFoundWhenThrowUnknownCargoException() {
		given(cargoFinder.require(any())).willReturn(cargo);
		assertThat(this.mvc.get().uri("/cargos/{trackingId}", "001"))
				.hasStatus(HttpStatus.OK);
	}

	@Test
	void register_shouldReturnCreated() throws Exception {
		// language=json
		var request = """
				{"originUnlocode":"DEHAM","destinationUnlocode":"SESTO","arrivalDeadline":"2024-12-09"}
				""";

		given(bookingService.book(any())).willReturn(cargo);

		assertThat(this.mvc
				.post()
				.uri("/cargos")
				.content(request)
				.contentType(MediaType.APPLICATION_JSON))
				.hasStatus(HttpStatus.CREATED);
	}

	@Test
	void assignItinerary_shouldReturnOk() throws Exception {
		// language=json
		var request = """
				{"legs":[{"voyageNumber":"CM005","fromUnLocode":"DEHAM","toUnLocode":"SESTO","fromDate":"2024-12-08","toDate":"2024-12-09"}]}""";

		given(cargoFinder.require(any())).willReturn(cargo);
		assertThat(this.mvc
				.put()
				.uri("/cargos/{trackingId}/itinerary", "001")
				.content(request)
				.contentType(MediaType.APPLICATION_JSON))
				.hasStatus(HttpStatus.OK);
	}

	@Test
	void assignItinerary_shouldReturnNotFoundWhenThrowUnknownCargoException() throws Exception {
		// language=json
		var request = """
				{"legs":[{"voyageNumber":"CM005","fromUnLocode":"DEHAM","toUnLocode":"SESTO","fromDate":"2024-12-08","toDate":"2024-12-09"}]}""";

		given(cargoFinder.require(any())).willThrow(new UnknownCargoException(TrackingId.of("001")));
		assertThat(this.mvc
				.put()
				.uri("/cargos/{trackingId}/itinerary", "001")
				.content(request)
				.contentType(MediaType.APPLICATION_JSON))
				.hasStatus(HttpStatus.NOT_FOUND);
	}

	@Test
	void changeDestination_shouldReturnOk() throws Exception {
		// language=json
		var request = """
				{"destination":"SESTO"}""";

		given(cargoFinder.require(any())).willReturn(cargo);
		assertThat(this.mvc
				.put()
				.uri("/cargos/{trackingId}/destination", "001")
				.content(request)
				.contentType(MediaType.APPLICATION_JSON))
				.hasStatus(HttpStatus.OK);
	}

	@Test
	void changeDestination_shouldReturnNotFoundWhenThrowUnknownCargoException() throws Exception {
		// language=json
		var request = """
				{"destination":"SESTO"}""";

		given(cargoFinder.require(any())).willThrow(new UnknownCargoException(TrackingId.of("001")));
		assertThat(this.mvc
				.put()
				.uri("/cargos/{trackingId}/destination", "001")
				.content(request)
				.contentType(MediaType.APPLICATION_JSON))
				.hasStatus(HttpStatus.NOT_FOUND);
	}

}