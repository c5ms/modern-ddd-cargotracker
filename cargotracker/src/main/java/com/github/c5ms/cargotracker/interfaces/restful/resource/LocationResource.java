package com.github.c5ms.cargotracker.interfaces.restful.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.github.c5ms.cargotracker.domain.model.location.LocationFinder;
import com.github.c5ms.cargotracker.interfaces.model.convertor.CargoConvertor;
import com.github.c5ms.cargotracker.interfaces.model.dto.LocationDto;

import java.util.List;


@Slf4j
@Tag(name = "location")
@RestController
@RequiredArgsConstructor
@RequestMapping("/locations")
public class LocationResource {

    private final LocationFinder locationFinder;
    private final CargoConvertor cargoConvertor;

    @Operation(summary = "list all locations")
    @GetMapping
    public List<LocationDto> list() {
        return locationFinder.listAll()
            .stream()
            .map(cargoConvertor::toDto)
            .toList();
    }


}
