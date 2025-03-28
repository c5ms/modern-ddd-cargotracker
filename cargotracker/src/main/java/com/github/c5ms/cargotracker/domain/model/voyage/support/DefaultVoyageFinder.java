package com.github.c5ms.cargotracker.domain.model.voyage.support;

import com.github.c5ms.cargotracker.domain.model.voyage.UnknownVoyageException;
import com.github.c5ms.cargotracker.domain.model.voyage.Voyage;
import com.github.c5ms.cargotracker.domain.model.voyage.VoyageFinder;
import com.github.c5ms.cargotracker.domain.model.voyage.VoyageNumber;
import com.github.c5ms.cargotracker.domain.model.voyage.VoyageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultVoyageFinder implements VoyageFinder {

    private final VoyageRepository voyageRepository;

    @Override
    public Optional<Voyage> find(VoyageNumber voyageNumber) {
        return voyageRepository.findByVoyageNumber(voyageNumber.getNumber());
    }

    @Override
    public Voyage require(VoyageNumber voyageNumber) throws UnknownVoyageException {
        return find(voyageNumber).orElseThrow(() -> new UnknownVoyageException(voyageNumber));
    }
}
