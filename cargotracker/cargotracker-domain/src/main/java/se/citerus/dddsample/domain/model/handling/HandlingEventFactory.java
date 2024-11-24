package se.citerus.dddsample.domain.model.handling;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import se.citerus.dddsample.domain.model.cargo.Cargo;
import se.citerus.dddsample.domain.model.cargo.CargoFinder;
import se.citerus.dddsample.domain.model.location.Location;
import se.citerus.dddsample.domain.model.location.LocationFinder;
import se.citerus.dddsample.domain.model.voyage.Voyage;
import se.citerus.dddsample.domain.model.voyage.VoyageFinder;

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
