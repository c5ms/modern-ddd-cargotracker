package se.citerus.dddsample.domain.model.location;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import se.citerus.dddsample.domain.model.location.support.DefaultLocationFinder;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DataJpaTest
@Import(DefaultLocationFinder.class)
class DefaultLocationFinderTest {

    @MockitoBean
    LocationRepository locationRepository;

    @Autowired
    LocationFinder locationFinder;

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
        Assertions.assertThrows(UnknownLocationException.class,() -> locationFinder.require(UnLocode.of("USCHI")) );
    }


    @Test
    public void listAll() {
        given(locationRepository.findAll()).willReturn(List.of());
       locationFinder.listAll();
    }

}