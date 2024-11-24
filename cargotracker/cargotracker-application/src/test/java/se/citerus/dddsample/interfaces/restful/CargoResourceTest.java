package se.citerus.dddsample.interfaces.restful;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import se.citerus.dddsample.application.service.BookingService;
import se.citerus.dddsample.domain.model.cargo.CargoFinder;
import se.citerus.dddsample.utils.TestCargoGenerator;
import se.citerus.dddsample.domain.model.cargo.TrackingId;
import se.citerus.dddsample.domain.model.cargo.UnknownCargoException;
import se.citerus.dddsample.infrastructure.initialize.SampleLocations;
import se.citerus.dddsample.infrastructure.initialize.SampleVoyages;
import se.citerus.dddsample.interfaces.model.request.CargoAssignRouteRequest;
import se.citerus.dddsample.interfaces.model.request.CargoDestinationChangeRequest;
import se.citerus.dddsample.interfaces.model.request.CargoRegisterRequest;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CargoResourceTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    CargoFinder cargoFinder;

    @MockitoBean
    BookingService bookingService;


    @Test
    void list_shouldReturnOK() throws Exception {
        when(cargoFinder.listAll()).thenReturn(List.of(TestCargoGenerator.emptyCargo()));
        this.mockMvc.perform(get("/cargos")).andExpect(status().isOk());
    }

    @Test
    void read_shouldReturnOK() throws Exception {
        when(cargoFinder.require(any())).thenThrow(new UnknownCargoException(TrackingId.of("001")));
        this.mockMvc.perform(get("/cargos/{trackingId}", "001")).andExpect(status().isNotFound());
    }

    @Test
    void read_shouldReturnNotFoundWhenThrowUnknownCargoException() throws Exception {
        when(cargoFinder.require(any())).thenReturn(TestCargoGenerator.emptyCargo());
        this.mockMvc.perform(get("/cargos/{trackingId}", "001")).andExpect(status().isOk());
    }

    @Test
    void register_shouldReturnCreated() throws Exception {
        when(bookingService.book(any())).thenReturn(TestCargoGenerator.emptyCargo());
        var request = createCargoRegisterRequest();
        this.mockMvc
            .perform(
                post("/cargos")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isCreated());
    }


    @Test
    void assignItinerary_shouldReturnOk() throws Exception {
        when(cargoFinder.require(any())).thenReturn(TestCargoGenerator.emptyCargo());
        var request = createCargoAssignRouteRequest();
        this.mockMvc
            .perform(
                put("/cargos/{trackingId}/itinerary", "001")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk());
    }

    @Test
    void assignItinerary_shouldReturnNotFoundWhenThrowUnknownCargoException() throws Exception {
        when(cargoFinder.require(any())).thenThrow(new UnknownCargoException(TrackingId.of("001")));
        var request = createCargoAssignRouteRequest();
        this.mockMvc
            .perform(
                put("/cargos/{trackingId}/itinerary", "001")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isNotFound());
    }


    @Test
    void changeDestination_shouldReturnOk() throws Exception {
        when(cargoFinder.require(any())).thenReturn(TestCargoGenerator.emptyCargo());
        var request = createCargoDestinationChangeRequest();
        this.mockMvc
            .perform(
                put("/cargos/{trackingId}/destination", "001")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk());
    }




    @Test
    void changeDestination_shouldReturnNotFoundWhenThrowUnknownCargoException() throws Exception {
        when(cargoFinder.require(any())).thenThrow(new UnknownCargoException(TrackingId.of("001")));
        var request = createCargoDestinationChangeRequest();
        this.mockMvc
            .perform(
                put("/cargos/{trackingId}/destination", "001")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isNotFound());
    }


    private static CargoDestinationChangeRequest createCargoDestinationChangeRequest() {
        return new CargoDestinationChangeRequest()
            .setDestination(SampleLocations.STOCKHOLM.getUnlocode());
    }

    private static CargoRegisterRequest createCargoRegisterRequest() {
        return new CargoRegisterRequest()
            .setArrivalDeadline(LocalDate.now().plusDays(1))
            .setOriginUnlocode(SampleLocations.HAMBURG.getUnlocode())
            .setDestinationUnlocode(SampleLocations.STOCKHOLM.getUnlocode());
    }


    private static CargoAssignRouteRequest createCargoAssignRouteRequest() {
        return new CargoAssignRouteRequest()
            .setLegs(List.of(new CargoAssignRouteRequest.Leg()
                .setFromDate(LocalDate.now())
                .setToDate(LocalDate.now().plusDays(1))
                .setFromUnLocode(SampleLocations.HAMBURG.getUnlocode())
                .setToUnLocode(SampleLocations.STOCKHOLM.getUnlocode())
                .setVoyageNumber(SampleVoyages.CM005.getVoyageNumber())));
    }

}