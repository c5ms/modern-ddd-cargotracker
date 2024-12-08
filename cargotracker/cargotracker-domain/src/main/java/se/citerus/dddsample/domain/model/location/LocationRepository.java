package se.citerus.dddsample.domain.model.location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, String> {

    @Query("from Location where unlocode.code=?1")
    Optional<Location> findByUnlocode(String unlocode);

}
