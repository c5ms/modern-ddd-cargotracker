package com.github.c5ms.cargotracker.domain.model.routeing;

import com.github.c5ms.cargotracker.domain.model.cargo.Itinerary;
import com.github.c5ms.cargotracker.domain.model.cargo.RouteSpecification;

import java.util.List;

/**
 * the routing is not under the context, please implement it in application layer( by calling inner resource directly)
 * or infrastructure layer (by calling external resource remotely) as a reusable component.
 */
public interface RoutingFinder {

    List<Itinerary> find(RouteSpecification routeSpecification);

}
