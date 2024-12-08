package se.citerus.dddsample.domain.model.location;

public class UnknownLocationException extends RuntimeException {

    private final UnLocode unlocode;

    public UnknownLocationException(final UnLocode unlocode) {
        this.unlocode = unlocode;
    }

    @Override
    public String getMessage() {
        return "No location with UN locode " + unlocode.getCode() + " exists in the system";
    }
}
