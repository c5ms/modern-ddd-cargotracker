package se.citerus.dddsample.interfaces.restful;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import se.citerus.dddsample.domain.model.location.LocationFinder;
import se.citerus.dddsample.infrastructure.initialize.SampleLocations;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LocationResourceTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    LocationFinder locationFinder;

    @Test
    void list_shouldReturnOK() throws Exception {
        when(locationFinder.listAll()).thenReturn(List.of(SampleLocations.HAMBURG));
        this.mockMvc.perform(get("/locations")).andExpect(status().isOk());
    }


}