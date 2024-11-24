package se.citerus.dddsample.application.service;

import se.citerus.dddsample.application.command.CargoAssignRouteCommand;
import se.citerus.dddsample.application.command.CargoChangeDestinationCommand;
import se.citerus.dddsample.application.command.CargoRegisterCommand;
import se.citerus.dddsample.domain.model.cargo.Cargo;
import se.citerus.dddsample.domain.model.cargo.TrackingId;

/**
 * Cargo booking service.
 */
public interface CargoService {

  Cargo book(CargoRegisterCommand command);

  void assignRoute(TrackingId trackingId, CargoAssignRouteCommand command);

  void changeDestination(TrackingId trackingId, CargoChangeDestinationCommand command);

  void inspectCargo(TrackingId trackingId);
}
