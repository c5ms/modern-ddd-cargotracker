package se.citerus.dddsample.domain.model.location;

import org.junit.jupiter.api.Test;

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
        var chicago = Location.of(UnLocode.of("USCHI"), "Chicago");
        chicago.setId(1L);

        var chicago2 = Location.of(UnLocode.of("USCHI"), "Chicago");
        chicago2.setId(2L);

        assertTrue(chicago.sameIdentityAs(chicago2));
    }
}