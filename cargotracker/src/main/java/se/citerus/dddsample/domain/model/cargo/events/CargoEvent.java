package se.citerus.dddsample.domain.model.cargo.events;

import se.citerus.dddsample.domain.model.cargo.Cargo;
import se.citerus.dddsample.domain.shared.DomainEvent;

public interface CargoEvent extends DomainEvent {
    Cargo getCargo();
}
