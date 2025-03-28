package com.github.c5ms.cargotracker.interfaces.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Getter
@Setter
@Schema(name = "Location")
@Accessors(chain = true)
public class LocationDto implements Serializable {
    private String unlocode;
    private String name;
}