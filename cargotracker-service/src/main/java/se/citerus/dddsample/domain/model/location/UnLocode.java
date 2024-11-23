package se.citerus.dddsample.domain.model.location;

import org.apache.commons.lang3.Validate;
import se.citerus.dddsample.domain.shared.ValueObject;

import java.util.regex.Pattern;

/**
 * United nations location code.
 * <p/>
 * <a href="http://www.unece.org/cefact/locode/">cefact locode</a>
 * <a href="http://www.unece.org/cefact/locode/DocColumnDescription.htm#LOCODE">LOCODE</a>
 */
public final class UnLocode implements ValueObject<UnLocode> {

    // Country code is exactly two letters.
    // Location code is usually three letters, but may contain the numbers 2-9 as well
    public static final String UN_LOCODE_REGEX="[a-zA-Z]{2}[a-zA-Z2-9]{3}";

    private static final Pattern UN_LOCODE_PATTERN = Pattern.compile(UN_LOCODE_REGEX);

    private final String unlocode;

    private UnLocode(final String countryAndLocation) {
        Validate.notNull(countryAndLocation, "Country and location may not be null");
        Validate.isTrue(UN_LOCODE_PATTERN.matcher(countryAndLocation).matches(), countryAndLocation + " is not a valid UN/LOCODE (does not match pattern)");
        this.unlocode = countryAndLocation.toUpperCase();
    }

    public static UnLocode of(final String countryAndLocation) {
        return new UnLocode(countryAndLocation);
    }


    /**
     * @return country code and location code concatenated, always upper case.
     */
    public String idString() {
        return unlocode;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UnLocode other = (UnLocode) o;

        return sameValueAs(other);
    }

    @Override
    public int hashCode() {
        return unlocode.hashCode();
    }

    @Override
    public boolean sameValueAs(UnLocode other) {
        return other != null && this.unlocode.equals(other.unlocode);
    }

    @Override
    public String toString() {
        return idString();
    }

}
