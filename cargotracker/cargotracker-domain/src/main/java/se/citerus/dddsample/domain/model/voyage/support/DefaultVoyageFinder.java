package se.citerus.dddsample.domain.model.voyage.support;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import se.citerus.dddsample.domain.model.voyage.*;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultVoyageFinder implements VoyageFinder {

    private final VoyageRepository voyageRepository;

    @Override
    public Optional<Voyage> find(VoyageNumber voyageNumber) {
        return voyageRepository.findByVoyageNumber(voyageNumber.idString());
    }

    @Override
    public Voyage require(VoyageNumber voyageNumber) throws UnknownVoyageException {
        return find(voyageNumber).orElseThrow(() -> new UnknownVoyageException(voyageNumber));
    }
}
