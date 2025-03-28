package com.github.c5ms.cargotracker.domain.model.location;

import java.util.Collection;
import java.util.Optional;

public interface LocationFinder {

    Collection<Location> listAll();

    Optional<Location> find(UnLocode unLocode);

    Location require(UnLocode unLocode) throws UnknownLocationException;

}
