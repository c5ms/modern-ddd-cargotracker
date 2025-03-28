package com.github.c5ms.cargotracker.domain.model.handling;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import com.github.c5ms.cargotracker.domain.model.cargo.TrackingId;
import com.github.c5ms.cargotracker.domain.model.location.UnLocode;
import com.github.c5ms.cargotracker.domain.model.voyage.VoyageNumber;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Builder
@RequiredArgsConstructor
public class HandlingReport  implements Serializable {
    private final Instant registrationTime;

    private final Instant completionTime;

    private final TrackingId trackingId;

    @Nullable
    private final VoyageNumber voyageNumber;

    private final HandlingEvent.Type type;

    private final UnLocode unLocode;
}