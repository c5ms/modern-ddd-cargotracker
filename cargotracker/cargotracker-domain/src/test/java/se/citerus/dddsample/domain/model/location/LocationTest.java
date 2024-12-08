package se.citerus.dddsample.domain.model.location;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LocationTest {

    @Test
    void of() {
        var location = Location.of("USCHI", "Chicago");
        assertEquals(0, location.getId());
        assertEquals("Chicago", location.getName());
        assertEquals("USCHI", location.getUnlocode().getCode());
    }

    @Test
    void sameIdentityAs() {
        assertThat(Location.of(UnLocode.of("USCHI"), "Chicago")
            .sameIdentityAs(Location.of(UnLocode.of("USCHI"), "Chicago")))
            .isTrue();

        assertThat(Location.of(UnLocode.of("USCHI"), "Chicago")
            .sameIdentityAs(null))
            .isFalse();
    }
}