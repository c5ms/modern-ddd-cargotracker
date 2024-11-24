package se.citerus.dddsample.domain.model.cargo;

import lombok.experimental.UtilityClass;
import se.citerus.dddsample.domain.model.location.Location;
import se.citerus.dddsample.infrastructure.initialize.SampleDataInitializer;
import se.citerus.dddsample.infrastructure.initialize.SampleLocations;

import java.time.Instant;

@UtilityClass
public class TestCargoGenerator {

    public  Cargo emptyCargo(){
        return Cargo.of(TrackingId.of("T001"), SampleLocations.HAMBURG,SampleLocations.CHICAGO, Instant.now());
    }

}
