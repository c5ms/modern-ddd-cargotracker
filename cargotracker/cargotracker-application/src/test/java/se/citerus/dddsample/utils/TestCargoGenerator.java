package se.citerus.dddsample.utils;

import lombok.experimental.UtilityClass;
import se.citerus.dddsample.domain.model.cargo.Cargo;
import se.citerus.dddsample.domain.model.cargo.TrackingId;
import se.citerus.dddsample.domain.model.location.Location;
import se.citerus.dddsample.infrastructure.initialize.SampleLocations;

import java.time.Instant;

@UtilityClass
public class TestCargoGenerator {

    public Cargo emptyCargo() {
        return Cargo.of(TrackingId.of("T001"), fromLocation(), toLocation(), Instant.now());
    }

    public Location fromLocation() {
        return SampleLocations.STOCKHOLM;
    }

    public Location toLocation() {
        return SampleLocations.TOKYO;
    }

    public Location changeLocation() {
        return SampleLocations.DALLAS;
    }

}
