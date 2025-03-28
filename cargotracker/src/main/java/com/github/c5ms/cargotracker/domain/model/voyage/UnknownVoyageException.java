package com.github.c5ms.cargotracker.domain.model.voyage;

/**
 * Thrown when trying to register an event with an unknown carrier movement id.
 */
public class UnknownVoyageException extends RuntimeException {

    private final VoyageNumber voyageNumber;

    public UnknownVoyageException(VoyageNumber voyageNumber) {
        this.voyageNumber = voyageNumber;
    }

    @Override
    public String getMessage() {
        return "No voyage with number " + voyageNumber.getNumber() + " exists in the system";
    }
}
