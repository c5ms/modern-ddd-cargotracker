package se.citerus.dddsample.domain.model.cargo;

/**
 * Thrown when trying to register an event with an unknown tracking id.
 */
public final class UnknownCargoException extends RuntimeException {

    private final TrackingId trackingId;

    /**
     * @param trackingId cargo tracking id
     */
    public UnknownCargoException(final TrackingId trackingId) {
        this.trackingId = trackingId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMessage() {
        return "No cargo with tracking id " + trackingId.getId() + " exists in the system";
    }
}
