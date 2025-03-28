package com.github.c5ms.cargotracker.domain.model.cargo.support;

import com.github.c5ms.cargotracker.domain.model.cargo.Cargo;
import com.github.c5ms.cargotracker.domain.model.cargo.CargoFinder;
import com.github.c5ms.cargotracker.domain.model.cargo.CargoRepository;
import com.github.c5ms.cargotracker.domain.model.cargo.TrackingId;
import com.github.c5ms.cargotracker.domain.model.cargo.UnknownCargoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultCargoFinder implements CargoFinder {

    private final CargoRepository cargoRepository;

    @Override
    public Collection<Cargo> listAll() {
        return cargoRepository.findAll();
    }

    @Override
    public Optional<Cargo> find(TrackingId trackingId) {
        return cargoRepository.findByTrackingId(trackingId.getId());
    }

    @Override
    public Cargo require(TrackingId trackingId) throws UnknownCargoException {
        return find(trackingId).orElseThrow(() -> new UnknownCargoException(trackingId));
    }
}
