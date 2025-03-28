package com.github.c5ms.cargotracker.application.command;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import com.github.c5ms.cargotracker.domain.model.cargo.Itinerary;

@Getter
@Builder
@RequiredArgsConstructor
public class CargoAssignRouteCommand {
    private final Itinerary itinerary;
}
