package com.github.c5ms.cargotracker.application.command;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import com.github.c5ms.cargotracker.domain.model.location.UnLocode;

@Getter
@Builder
@RequiredArgsConstructor
public class CargoDestinationChangeCommand {
    private final UnLocode destination;
}
