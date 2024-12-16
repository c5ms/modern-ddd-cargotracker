package se.citerus.dddsample.interfaces.restful.resource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import se.citerus.dddsample.domain.model.location.LocationFinder;
import se.citerus.dddsample.infrastructure.configure.CargoTrackerJacksonConfigure;
import se.citerus.dddsample.interfaces.configure.CargoTrackerInterfacesConfigure;
import se.citerus.dddsample.interfaces.model.convertor.CargoConvertor;
import se.citerus.dddsample.sample.SampleLocations;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

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