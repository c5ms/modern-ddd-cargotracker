package com.github.c5ms.cargotracker.domain.model.location;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.Validate;

import java.util.regex.Pattern;

/**
 * United nations location code.
 * <p/>
 * <a href="http://www.unece.org/cefact/locode/">cefact locode</a>
 * <a href="http://www.unece.org/cefact/locode/DocColumnDescription.htm#LOCODE">LOCODE</a>
 */
@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class UnLocode {

    // Country code is exactly two letters.
    // Location code is usually three letters, but may contain the numbers 2-9 as well
    public static final String UN_LOCODE_REGEX = "[a-zA-Z]{2}[a-zA-Z2-9]{3}";
    private static final Pattern UN_LOCODE_PATTERN = Pattern.compile(UN_LOCODE_REGEX);

    @Column(name = "unlocode", nullable = false, unique = true, updatable = false)
    private String code;

    private UnLocode(final String countryAndLocation) {
        Validate.notNull(countryAndLocation, "Country and location may not be null");
        Validate.isTrue(UN_LOCODE_PATTERN.matcher(countryAndLocation).matches(), countryAndLocation + " is not a valid UN/LOCODE (does not match pattern)");
        this.code = countryAndLocation.toUpperCase();
    }

    public static UnLocode of(final String countryAndLocation) {
        return new UnLocode(countryAndLocation);
    }
}
