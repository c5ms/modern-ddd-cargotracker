package se.citerus.dddsample.domain.model.voyage;

import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.Validate;
import se.citerus.dddsample.domain.shared.ValueObject;

/**
 * Identifies a voyage.
 */
@EqualsAndHashCode
public class VoyageNumber implements ValueObject<VoyageNumber> {

    private final String number;

    private VoyageNumber(String number) {
        Validate.notNull(number, "number should not be null");

        this.number = number;
    }

    public static VoyageNumber of(String number) {
        return new VoyageNumber(number);
    }

    @Override
    public boolean sameValueAs(VoyageNumber other) {
        if (null == other) {
            return false;
        }
        return this.number.equals(other.number);
    }

    @Override
    public String toString() {
        return number;
    }

    public String idString() {
        return number;
    }

}
