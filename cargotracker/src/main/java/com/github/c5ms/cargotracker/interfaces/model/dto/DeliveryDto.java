package com.github.c5ms.cargotracker.interfaces.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.github.c5ms.cargotracker.domain.model.cargo.HandlingActivity;
import com.github.c5ms.cargotracker.domain.model.cargo.RoutingStatus;
import com.github.c5ms.cargotracker.domain.model.cargo.TransportStatus;
import com.github.c5ms.cargotracker.domain.model.location.Location;

import java.time.Instant;

@Getter
@Setter
@Schema(name = "Delivery")
@Accessors(chain = true)
public class DeliveryDto {

    private boolean misdirected;

    private Instant eta;

    private Instant calculatedAt;

    private boolean isUnloadedAtDestination;

    private RoutingStatus routingStatus;

    private HandlingActivity nextExpectedActivity;

    private TransportStatus transportStatus;

    private String currentVoyageNumber;

    private Location lastKnownLocation;

}
