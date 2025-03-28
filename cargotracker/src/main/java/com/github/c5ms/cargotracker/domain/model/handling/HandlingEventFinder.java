package com.github.c5ms.cargotracker.domain.model.handling;

import com.github.c5ms.cargotracker.domain.model.cargo.TrackingId;

public interface HandlingEventFinder {

     HandlingHistory handlingHistoryOf(TrackingId trackingId) ;

}
