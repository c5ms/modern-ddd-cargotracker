package se.citerus.dddsample.application.service;

import se.citerus.dddsample.domain.model.cargo.TrackingId;

public interface CargoInspector {
    void inspectCargo(TrackingId trackingId);
}
