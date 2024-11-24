package se.citerus.dddsample.interfaces.restful.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import se.citerus.dddsample.application.service.BookingService;
import se.citerus.dddsample.domain.model.cargo.CargoFinder;
import se.citerus.dddsample.domain.model.cargo.TrackingId;
import se.citerus.dddsample.domain.model.cargo.UnknownCargoException;
import se.citerus.dddsample.interfaces.model.convertor.CargoConvertor;
import se.citerus.dddsample.interfaces.model.dto.CargoDto;
import se.citerus.dddsample.interfaces.model.request.CargoAssignRouteRequest;
import se.citerus.dddsample.interfaces.model.request.CargoChangeDestinationRequest;
import se.citerus.dddsample.interfaces.model.request.CargoRegisterRequest;

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
    public CargoDto changeDestination(@PathVariable("trackingId") TrackingId trackingId, @Validated @RequestBody CargoChangeDestinationRequest request) {
        var command = cargoConvertor.toCommand(request);
        bookingService.changeDestination(trackingId, command);
        var cargo = cargoFinder.require(trackingId);
        return cargoConvertor.toDto(cargo);
    }


}
