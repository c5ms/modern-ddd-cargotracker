package se.citerus.dddsample.interfaces.convertor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import se.citerus.dddsample.application.command.CargoAssignRouteCommand;
import se.citerus.dddsample.application.command.CargoChangeDestinationCommand;
import se.citerus.dddsample.application.command.CargoRegisterCommand;
import se.citerus.dddsample.domain.model.cargo.Cargo;
import se.citerus.dddsample.domain.model.cargo.Delivery;
import se.citerus.dddsample.domain.model.cargo.Itinerary;
import se.citerus.dddsample.domain.model.cargo.Leg;
import se.citerus.dddsample.domain.model.location.Location;
import se.citerus.dddsample.domain.model.location.LocationFinder;
import se.citerus.dddsample.domain.model.location.UnLocode;
import se.citerus.dddsample.domain.model.voyage.Voyage;
import se.citerus.dddsample.domain.model.voyage.VoyageFinder;
import se.citerus.dddsample.domain.model.voyage.VoyageNumber;
import se.citerus.dddsample.interfaces.model.dto.CargoDto;
import se.citerus.dddsample.interfaces.model.dto.DeliveryDto;
import se.citerus.dddsample.interfaces.model.dto.LegDto;
import se.citerus.dddsample.interfaces.model.dto.LocationDto;
import se.citerus.dddsample.interfaces.model.request.CargoAssignRouteRequest;
import se.citerus.dddsample.interfaces.model.request.CargoChangeDestinationRequest;
import se.citerus.dddsample.interfaces.model.request.CargoRegisterRequest;

import java.time.Instant;
import java.time.ZoneOffset;

@Component
@RequiredArgsConstructor
public class CargoConvertor {

    private final VoyageFinder voyageFinder;
    private final LocationFinder locationFinder;

    public CargoRegisterCommand toCommand(CargoRegisterRequest request) {
        return CargoRegisterCommand.builder()
            .origin(UnLocode.of(request.getOriginUnlocode()))
            .destination(UnLocode.of(request.getDestinationUnlocode()))
            .arrivalDeadline(request.getArrivalDeadline().atStartOfDay().toInstant(ZoneOffset.UTC))
            .build();
    }

    public CargoAssignRouteCommand toCommand(CargoAssignRouteRequest request) {
        return CargoAssignRouteCommand.builder()
            .itinerary(Itinerary.of(request.getLegs().stream().map(this::toValue).toList()))
            .build();
    }

    public CargoChangeDestinationCommand toCommand(CargoChangeDestinationRequest request) {
        return CargoChangeDestinationCommand.builder()
            .destination(UnLocode.of(request.getDestination()))
            .build();
    }

    private Leg toValue(CargoAssignRouteRequest.Leg dto) {
        final VoyageNumber voyageNumber = VoyageNumber.of(dto.getVoyageNumber());
        final Voyage voyage = voyageFinder.require(voyageNumber);
        final Location from = locationFinder.require(UnLocode.of(dto.getFromUnLocode()));
        final Location to = locationFinder.require(UnLocode.of(dto.getToUnLocode()));
        final Instant fromDate = dto.getFromDate().atStartOfDay().toInstant(ZoneOffset.UTC);
        final Instant toDate = dto.getToDate().atStartOfDay().toInstant(ZoneOffset.UTC);
        return Leg.of(voyage, from, to, fromDate, toDate);
    }

    public CargoDto toDto(Cargo cargo) {
        return new CargoDto()
            .setTrackingId(cargo.getTrackingId())
            .setOrigin(toDto(cargo.getRouteSpecification().getOrigin()))
            .setDestination(toDto(cargo.getRouteSpecification().getDestination()))
            .setArrivalDeadline(cargo.getRouteSpecification().getArrivalDeadline())
            .setLegs(cargo.getLegs().stream().map(this::toDto).toList())
            .setDelivery(toDto(cargo.getDelivery()))
            ;
    }

    private DeliveryDto toDto(Delivery delivery) {
        return new DeliveryDto()
            .setEta(delivery.getEta())
            .setCalculatedAt(delivery.getCalculatedAt())
            .setMisdirected(delivery.isMisdirected())
            .setCurrentVoyageNumber(delivery.getCurrentVoyage().map(Voyage::getVoyageNumber).orElse(null))
            .setLastKnownLocation(delivery.getLastKnownLocation())
            .setRoutingStatus(delivery.getRoutingStatus())
            .setNextExpectedActivity(delivery.getNextExpectedActivity())
            ;
    }

    private LegDto toDto(Leg leg) {
        return new LegDto()
            .setVoyageNumber(leg.voyage().getVoyageNumber())
            .setFrom(leg.getLoadLocation().idString())
            .setTo(leg.getUnloadLocation().idString())
            .setUnloadTime(leg.unloadTime())
            .setLoadTime(leg.loadTime())
            ;
    }

    private LocationDto toDto(Location origin) {
        return new LocationDto().setUnlocode(origin.getUnlocode()).setName(origin.getName());
    }

}
