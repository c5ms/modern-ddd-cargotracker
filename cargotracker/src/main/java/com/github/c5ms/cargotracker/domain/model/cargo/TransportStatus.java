package com.github.c5ms.cargotracker.domain.model.cargo;

/**
 * Represents the different transport statuses for a cargo.
 */
public enum TransportStatus {
    NOT_RECEIVED,
    IN_PORT,
    ONBOARD_CARRIER,
    CLAIMED,
    UNKNOWN;
}
