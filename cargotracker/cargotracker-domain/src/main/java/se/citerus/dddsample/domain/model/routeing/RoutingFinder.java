package se.citerus.dddsample.domain.model.routeing;

import se.citerus.dddsample.domain.model.cargo.Itinerary;
import se.citerus.dddsample.domain.model.cargo.RouteSpecification;

import java.util.List;

/**
 * the routing is not under the context, please implement it in application layer( by calling inner resource directly)
 * or infrastructure layer (by calling external resource remotely) as a reusable component.
 */
public interface RoutingFinder {

    List<Itinerary> find(RouteSpecification routeSpecification);

}
