package se.citerus.dddsample.domain.model.location;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LocationRepository extends CrudRepository<Location, String> {

    Optional<Location> findByUnlocode(String unlocode);

}
