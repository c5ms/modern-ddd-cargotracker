package se.citerus.dddsample.domain.model.voyage;

import se.citerus.dddsample.domain.model.handling.CannotCreateHandlingEventException;

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
        return "No voyage with number " + voyageNumber.idString() + " exists in the system";
    }
}
