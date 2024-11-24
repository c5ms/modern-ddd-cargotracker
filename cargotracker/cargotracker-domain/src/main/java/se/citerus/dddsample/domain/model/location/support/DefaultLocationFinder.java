package se.citerus.dddsample.domain.model.location.support;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import se.citerus.dddsample.domain.model.location.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultLocationFinder implements LocationFinder {

    private final LocationRepository locationRepository;

    @Override
    public Collection<Location> listAll() {
        return locationRepository.findAll();
    }

    @Override
    public Optional<Location> find(UnLocode unLocode) {
        return locationRepository.findByUnlocode(unLocode.idString());
    }

    @Override
    public Location require(UnLocode unLocode) throws UnknownLocationException {
        return this.find(unLocode).orElseThrow(() -> new UnknownLocationException(unLocode));
    }

}
