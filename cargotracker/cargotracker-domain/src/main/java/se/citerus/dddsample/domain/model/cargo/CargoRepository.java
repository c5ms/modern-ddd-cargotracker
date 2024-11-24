package se.citerus.dddsample.domain.model.cargo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CargoRepository extends JpaRepository<Cargo, Long> {

    Optional<Cargo> findByTrackingId(String trackingId);
}
