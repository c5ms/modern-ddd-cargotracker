package se.citerus.dddsample.application.command;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import se.citerus.dddsample.domain.model.cargo.Itinerary;
import se.citerus.dddsample.domain.model.cargo.TrackingId;

@Getter
@Builder
@RequiredArgsConstructor
public class CargoAssignRouteCommand {
    private final Itinerary itinerary;
}
