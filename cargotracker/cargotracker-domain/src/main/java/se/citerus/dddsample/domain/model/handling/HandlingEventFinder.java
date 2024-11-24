package se.citerus.dddsample.domain.model.handling;

import se.citerus.dddsample.domain.model.cargo.TrackingId;
import se.citerus.dddsample.domain.model.location.Location;
import se.citerus.dddsample.domain.model.location.UnLocode;
import se.citerus.dddsample.domain.model.location.UnknownLocationException;

import java.util.Collection;
import java.util.Optional;

public interface HandlingEventFinder {

     HandlingHistory handlingHistoryOf(TrackingId trackingId) ;

}
