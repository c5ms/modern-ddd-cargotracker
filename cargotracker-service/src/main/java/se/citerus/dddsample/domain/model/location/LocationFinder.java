package se.citerus.dddsample.domain.model.location;

import java.util.Optional;

public interface LocationFinder {

    Optional<Location> find(UnLocode unLocode);

    Location require(UnLocode unLocode) throws UnknownLocationException;
}
