package com.github.c5ms.cargotracker.domain.model.cargo.events;

import com.github.c5ms.cargotracker.domain.model.cargo.Cargo;
import com.github.c5ms.cargotracker.domain.shared.DomainEvent;

public interface CargoEvent extends DomainEvent {
    Cargo getCargo();
}
