package com.github.c5ms.cargotracker.domain.model.cargo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CargoRepository extends JpaRepository<Cargo, Long> {

    @Query("from  Cargo where trackingId.id=?1")
    Optional<Cargo> findByTrackingId(String trackingId);
}
