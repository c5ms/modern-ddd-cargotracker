package se.citerus.dddsample.domain.model.handling;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.citerus.dddsample.domain.model.cargo.Cargo;
import se.citerus.dddsample.domain.model.cargo.TrackingId;

import java.util.Collection;
import java.util.Optional;

/**
 * Handling event repository.
 */
public interface HandlingEventRepository extends JpaRepository<HandlingEvent,Long> {

    @Query("from HandlingEvent e where e.cargo.trackingId=:trackingId")
    Collection<HandlingEvent> findByTrackingId(@Param("trackingId") String trackingId);

}
