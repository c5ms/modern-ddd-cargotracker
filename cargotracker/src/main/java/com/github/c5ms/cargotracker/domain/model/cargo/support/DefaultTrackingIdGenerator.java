package com.github.c5ms.cargotracker.domain.model.cargo.support;

import org.springframework.stereotype.Component;
import com.github.c5ms.cargotracker.domain.model.cargo.TrackingId;
import com.github.c5ms.cargotracker.domain.model.cargo.TrackingIdGenerator;

import java.util.UUID;

@Component
public class DefaultTrackingIdGenerator implements TrackingIdGenerator {

    @Override
    public TrackingId next() {
        var serial = UUID.randomUUID().toString().substring(0, 8);
        serial = serial.toUpperCase();
        return TrackingId.of(serial);
    }
}
