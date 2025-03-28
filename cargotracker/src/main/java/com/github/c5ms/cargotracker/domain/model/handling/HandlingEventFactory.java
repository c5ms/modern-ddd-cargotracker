package com.github.c5ms.cargotracker.domain.model.handling;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.github.c5ms.cargotracker.domain.model.cargo.Cargo;
import com.github.c5ms.cargotracker.domain.model.cargo.CargoFinder;
import com.github.c5ms.cargotracker.domain.model.location.Location;
import com.github.c5ms.cargotracker.domain.model.location.LocationFinder;
import com.github.c5ms.cargotracker.domain.model.voyage.Voyage;
import com.github.c5ms.cargotracker.domain.model.voyage.VoyageFinder;

import java.time.Instant;
import java.util.Optional;


/**
 * Creates handling events.
 */
@RequiredArgsConstructor
@Component
public class HandlingEventFactory {

    private final CargoFinder cargoFinder;
    private final VoyageFinder voyageFinder;
    private final LocationFinder locationFinder;

    public HandlingEvent createHandlingEvent(HandlingReport report) {
        final Cargo cargo = cargoFinder.require(report.getTrackingId());
        final Location location = locationFinder.require(report.getUnLocode());
        final Instant completionTime = report.getCompletionTime();
        final Instant registrationTime = report.getRegistrationTime();
        final HandlingEvent.Type type = report.getType();
        final Voyage voyage = Optional.ofNullable(report.getVoyageNumber()).map(voyageFinder::require).orElse(null);
        if (voyage == null) {
            return HandlingEvent.of(cargo, completionTime, registrationTime, type, location);
        } else {
            return HandlingEvent.createHandlingEvent(cargo, completionTime, registrationTime, type, location, voyage);
        }
    }


}
