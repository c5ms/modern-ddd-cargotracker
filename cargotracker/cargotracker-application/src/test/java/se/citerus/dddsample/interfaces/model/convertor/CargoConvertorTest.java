package se.citerus.dddsample.interfaces.model.convertor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import se.citerus.dddsample.domain.model.location.LocationFinder;
import se.citerus.dddsample.domain.model.voyage.VoyageFinder;
import se.citerus.dddsample.interfaces.model.request.CargoRegisterRequest;
import se.citerus.dddsample.sample.SampleLocations;

import java.time.LocalDate;
import java.time.ZoneOffset;

@SpringBootTest(classes =CargoConvertor.class )
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
            .setOriginUnlocode(SampleLocations.HAMBURG.getUnlocode())
            .setDestinationUnlocode(SampleLocations.STOCKHOLM.getUnlocode());
        var command = cargoConvertor.toCommand(request);

        Assertions.assertNotNull(command);
        Assertions.assertEquals(LocalDate.now().plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC) , command.getArrivalDeadline());
        Assertions.assertEquals(SampleLocations.HAMBURG.getUnlocode(), command.getOrigin().idString());
        Assertions.assertEquals(SampleLocations.STOCKHOLM.getUnlocode(), command.getDestination().idString());
    }

}

