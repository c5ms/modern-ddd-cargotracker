package se.citerus.dddsample.infrastructure.initialize;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import se.citerus.dddsample.domain.model.location.LocationRepository;
import se.citerus.dddsample.domain.model.voyage.VoyageRepository;

@Slf4j
@RequiredArgsConstructor
public class SampleDataInitializer {

    private final LocationRepository locationRepository;
    private final VoyageRepository voyageRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void on(ApplicationReadyEvent event) {
        log.info("Initializing sample data...");
        locationRepository.saveAll(SampleLocations.getAll());
        voyageRepository.saveAll(SampleVoyages.getAll());
    }

}
