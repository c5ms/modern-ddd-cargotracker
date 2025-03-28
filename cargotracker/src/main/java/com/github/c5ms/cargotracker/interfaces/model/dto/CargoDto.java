package com.github.c5ms.cargotracker.interfaces.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@Schema(name = "Cargo")
@Accessors(chain = true)
public class CargoDto implements Serializable {
    private String trackingId;
    private String origin;
    private String destination;
    private Instant arrivalDeadline;
}