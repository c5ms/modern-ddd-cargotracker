package com.github.c5ms.cargotracker.application.service;

import com.github.c5ms.cargotracker.domain.model.cargo.TrackingId;

public interface CargoInspector {
    void inspectCargo(TrackingId trackingId);
}
