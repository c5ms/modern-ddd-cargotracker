package se.citerus.dddsample.domain.model.cargo.support;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import se.citerus.dddsample.domain.model.cargo.*;

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
        return cargoRepository.findByTrackingId(trackingId.idString());
    }

    @Override
    public Cargo require(TrackingId trackingId) throws UnknownCargoException {
        return find(trackingId).orElseThrow(() -> new UnknownCargoException(trackingId));
    }
}
