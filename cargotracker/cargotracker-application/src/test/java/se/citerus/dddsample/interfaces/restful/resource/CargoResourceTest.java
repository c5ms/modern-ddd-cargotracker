package se.citerus.dddsample.interfaces.restful.resource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import se.citerus.dddsample.domain.model.cargo.CargoFinder;
import se.citerus.dddsample.domain.model.cargo.TestCargoGenerator;
import se.citerus.dddsample.domain.model.cargo.TrackingId;
import se.citerus.dddsample.domain.model.cargo.UnknownCargoException;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CargoResourceTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    CargoFinder cargoFinder;

    @Test
    void shouldReturnOK() throws Exception {
        when(cargoFinder.listAll()).thenReturn(List.of(TestCargoGenerator.emptyCargo()));
        this.mockMvc.perform(get("/cargos"))
            .andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotFound() throws Exception {
        when(cargoFinder.require(any())).thenThrow(new UnknownCargoException(TrackingId.of("001")));
        this.mockMvc.perform(get("/cargos/001"))
            .andExpect(status().isNotFound());
    }

}