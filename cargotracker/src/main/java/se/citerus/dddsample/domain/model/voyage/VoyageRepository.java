package se.citerus.dddsample.domain.model.voyage;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface VoyageRepository extends CrudRepository<Voyage, Long> {

    Optional<Voyage> findByVoyageNumber(String voyageNumber);

}
