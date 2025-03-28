package com.github.c5ms.cargotracker.domain.model.voyage;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.Validate;

/**
 * Identifies a voyage.
 */
@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VoyageNumber {

    @Column(name = "voyage_number", unique = true)
    private String number;

    private VoyageNumber(String number) {
        Validate.notNull(number, "number should not be null");
        this.number = number;
    }

    public static VoyageNumber of(String number) {
        return new VoyageNumber(number);
    }

    public static VoyageNumber empty() {
        return new VoyageNumber("");
    }
}
