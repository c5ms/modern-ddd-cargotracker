package se.citerus.dddsample.infrastructure.initialize;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import se.citerus.dddsample.domain.model.location.LocationRepository;
import se.citerus.dddsample.domain.model.voyage.VoyageRepository;

@Slf4j
@RequiredArgsConstructor
public class SampleDataInitializer implements InitializingBean {

    private final LocationRepository locationRepository;
    private final VoyageRepository voyageRepository;

    @Override
    public synchronized void afterPropertiesSet() {
  try {
      log.info("Initializing sample data...");
      locationRepository.saveAll(SampleLocations.getAll());
      voyageRepository.saveAll(SampleVoyages.getAll());
  }catch (Exception e ){
      log.warn("fail",e);
  }
    }

}
