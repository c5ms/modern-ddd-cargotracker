package se.citerus.dddsample.domain.model.handling;

import se.citerus.dddsample.domain.model.cargo.TrackingId;

public interface HandlingEventFinder {

     HandlingHistory handlingHistoryOf(TrackingId trackingId) ;

}
