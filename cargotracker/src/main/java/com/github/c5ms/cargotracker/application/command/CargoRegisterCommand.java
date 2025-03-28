package com.github.c5ms.cargotracker.application.command;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import com.github.c5ms.cargotracker.domain.model.location.UnLocode;

import java.time.Instant;


@Getter
@Builder
@RequiredArgsConstructor
public class CargoRegisterCommand {
    private final UnLocode origin;
    private final UnLocode destination;
    private final Instant arrivalDeadline;
}
