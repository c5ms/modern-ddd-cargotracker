package com.github.c5ms.cargotracker.domain.model.location.support;

import com.github.c5ms.cargotracker.domain.model.location.Location;
import com.github.c5ms.cargotracker.domain.model.location.LocationFinder;
import com.github.c5ms.cargotracker.domain.model.location.LocationRepository;
import com.github.c5ms.cargotracker.domain.model.location.UnLocode;
import com.github.c5ms.cargotracker.domain.model.location.UnknownLocationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
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
        return locationRepository.findByUnlocode(unLocode.getCode());
    }

    @Override
    public Location require(UnLocode unLocode) throws UnknownLocationException {
        return this.find(unLocode).orElseThrow(() -> new UnknownLocationException(unLocode));
    }

}
