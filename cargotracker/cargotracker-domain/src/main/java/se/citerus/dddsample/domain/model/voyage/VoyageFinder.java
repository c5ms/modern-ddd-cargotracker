package se.citerus.dddsample.domain.model.voyage;

import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface VoyageFinder {

    Optional<Voyage> find(VoyageNumber voyageNumber);

    Voyage require( VoyageNumber voyageNumber) throws UnknownVoyageException;

}
