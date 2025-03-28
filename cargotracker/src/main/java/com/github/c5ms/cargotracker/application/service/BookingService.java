package com.github.c5ms.cargotracker.application.service;

import com.github.c5ms.cargotracker.application.command.CargoAssignRouteCommand;
import com.github.c5ms.cargotracker.application.command.CargoDestinationChangeCommand;
import com.github.c5ms.cargotracker.application.command.CargoRegisterCommand;
import com.github.c5ms.cargotracker.domain.model.cargo.Cargo;
import com.github.c5ms.cargotracker.domain.model.cargo.TrackingId;

/**
 * Cargo booking service.
 */
public interface BookingService {

    Cargo book(CargoRegisterCommand command);

    void assignRoute(TrackingId trackingId, CargoAssignRouteCommand command);

    void changeDestination(TrackingId trackingId, CargoDestinationChangeCommand command);
}
