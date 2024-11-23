package se.citerus.dddsample.domain.model.cargo;

import java.util.Collection;
import java.util.Optional;

public interface CargoFinder {

    Collection<Cargo> listAll();


    Optional<Cargo> find(TrackingId trackingId);


    Cargo require(TrackingId trackingId) throws UnknownCargoException;

}
