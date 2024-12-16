package se.citerus.dddsample.domain.model.location;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.citerus.dddsample.domain.model.location.support.DefaultLocationFinder;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class DefaultLocationFinderTest {

    LocationRepository locationRepository;
    LocationFinder locationFinder;

    @BeforeEach
    void setUp() {
        locationRepository = mock(LocationRepository.class);
        locationFinder = new DefaultLocationFinder(locationRepository);
    }

    @Test
    public void require() {
        var location = mock(Location.class);
        given(locationRepository.findByUnlocode("USCHI")).willReturn(Optional.of(location));

        var find = locationFinder.require(UnLocode.of("USCHI"));
        Assertions.assertNotNull(find);
        Assertions.assertEquals(location, find);
    }

    @Test
    public void require2_unknown() {
        given(locationRepository.findByUnlocode("USCHI")).willReturn(Optional.empty());
        Assertions.assertThrows(UnknownLocationException.class, () -> locationFinder.require(UnLocode.of("USCHI")));
    }


    @Test
    public void listAll() {
        var location1 = mock(Location.class);
        var location2 = mock(Location.class);

        given(locationRepository.findAll()).willReturn(List.of(location1, location2));

        var all = locationFinder.listAll();
        Assertions.assertNotNull(all);
        Assertions.assertEquals(List.of(location1, location2), all);
    }

}