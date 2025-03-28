package com.github.c5ms.cargotracker.interfaces.restful.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.github.c5ms.cargotracker.application.service.BookingService;
import com.github.c5ms.cargotracker.domain.model.cargo.CargoFinder;
import com.github.c5ms.cargotracker.domain.model.cargo.TrackingId;
import com.github.c5ms.cargotracker.domain.model.cargo.UnknownCargoException;
import com.github.c5ms.cargotracker.interfaces.model.convertor.CargoConvertor;
import com.github.c5ms.cargotracker.interfaces.model.dto.CargoDto;
import com.github.c5ms.cargotracker.interfaces.model.request.CargoAssignRouteRequest;
import com.github.c5ms.cargotracker.interfaces.model.request.CargoDestinationChangeRequest;
import com.github.c5ms.cargotracker.interfaces.model.request.CargoRegisterRequest;

import java.util.List;


@Slf4j
@Tag(name = "cargo")
@RestController
@RequiredArgsConstructor
@RequestMapping("/cargos")
public class CargoResource {

    private final CargoFinder cargoFinder;
    private final CargoConvertor cargoConvertor;
    private final BookingService bookingService;

    @ExceptionHandler(UnknownCargoException.class)
    ResponseEntity<Void> on(UnknownCargoException ignored) {
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "list all cargos")
    @GetMapping
    public List<CargoDto> list() {
        return cargoFinder.listAll()
            .stream()
            .map(cargoConvertor::toDto)
            .toList();
    }

    @Operation(summary = "read cargo")
    @ApiResponse(responseCode = "404", description = "cargo not found")
    @GetMapping(value = "/{trackingId}")
    public CargoDto read(@PathVariable("trackingId") TrackingId trackingId) {
        var cargo = cargoFinder.require(trackingId);
        return cargoConvertor.toDto(cargo);
    }

    @Operation(summary = "register cargo")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CargoDto register(@Validated @RequestBody CargoRegisterRequest request) {
        var command = cargoConvertor.toCommand(request);
        var cargo = bookingService.book(command);
        return cargoConvertor.toDto(cargo);
    }


    @Operation(summary = "assign itinerary to cargo")
    @ApiResponse(responseCode = "404", description = "cargo not found")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/{trackingId}/itinerary")
    public CargoDto assignItinerary(@PathVariable("trackingId") TrackingId trackingId, @Validated @RequestBody CargoAssignRouteRequest request) {
        var command = cargoConvertor.toCommand(request);
        bookingService.assignRoute(trackingId, command);
        var cargo = cargoFinder.require(trackingId);
        return cargoConvertor.toDto(cargo);
    }

    @Operation(summary = "change destination to cargo")
    @ApiResponse(responseCode = "404", description = "cargo not found")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/{trackingId}/destination")
    public CargoDto changeDestination(@PathVariable("trackingId") TrackingId trackingId, @Validated @RequestBody CargoDestinationChangeRequest request) {
        var command = cargoConvertor.toCommand(request);
        bookingService.changeDestination(trackingId, command);
        var cargo = cargoFinder.require(trackingId);
        return cargoConvertor.toDto(cargo);
    }


}
