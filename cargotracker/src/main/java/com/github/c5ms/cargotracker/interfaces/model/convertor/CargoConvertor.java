package com.github.c5ms.cargotracker.interfaces.model.convertor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.github.c5ms.cargotracker.application.command.CargoAssignRouteCommand;
import com.github.c5ms.cargotracker.application.command.CargoDestinationChangeCommand;
import com.github.c5ms.cargotracker.application.command.CargoRegisterCommand;
import com.github.c5ms.cargotracker.domain.model.cargo.Cargo;
import com.github.c5ms.cargotracker.domain.model.cargo.Itinerary;
import com.github.c5ms.cargotracker.domain.model.cargo.Leg;
import com.github.c5ms.cargotracker.domain.model.location.Location;
import com.github.c5ms.cargotracker.domain.model.location.LocationFinder;
import com.github.c5ms.cargotracker.domain.model.location.UnLocode;
import com.github.c5ms.cargotracker.domain.model.voyage.Voyage;
import com.github.c5ms.cargotracker.domain.model.voyage.VoyageFinder;
import com.github.c5ms.cargotracker.domain.model.voyage.VoyageNumber;
import com.github.c5ms.cargotracker.interfaces.model.dto.CargoDto;
import com.github.c5ms.cargotracker.interfaces.model.dto.LocationDto;
import com.github.c5ms.cargotracker.interfaces.model.request.CargoAssignRouteRequest;
import com.github.c5ms.cargotracker.interfaces.model.request.CargoDestinationChangeRequest;
import com.github.c5ms.cargotracker.interfaces.model.request.CargoRegisterRequest;

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

    public CargoDestinationChangeCommand toCommand(CargoDestinationChangeRequest request) {
        return CargoDestinationChangeCommand.builder()
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
            .setTrackingId(cargo.getTrackingId().getId())
            .setOrigin(cargo.getRouteSpecification().getOrigin().getUnlocode().getCode())
            .setDestination(cargo.getRouteSpecification().getDestination().getUnlocode().getCode())
            .setArrivalDeadline(cargo.getRouteSpecification().getArrivalDeadline())
            ;
    }

    public LocationDto toDto(Location origin) {
        return new LocationDto()
            .setUnlocode(origin.getUnlocode().getCode())
            .setName(origin.getName());
    }

}
