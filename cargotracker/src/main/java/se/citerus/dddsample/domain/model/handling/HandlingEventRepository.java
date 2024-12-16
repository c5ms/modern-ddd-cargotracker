package se.citerus.dddsample.domain.model.handling;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

/**
 * Handling event repository.
 */
public interface HandlingEventRepository extends JpaRepository<HandlingEvent,Long> {

    @Query("from HandlingEvent e where e.cargo.trackingId=:trackingId")
    Collection<HandlingEvent> findByTrackingId(@Param("trackingId") String trackingId);

}
