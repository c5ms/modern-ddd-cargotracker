package se.citerus.dddsample.infrastructure.configure;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.citerus.dddsample.domain.model.location.LocationRepository;
import se.citerus.dddsample.domain.model.voyage.VoyageRepository;
import se.citerus.dddsample.infrastructure.initialize.SampleDataInitializer;


@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({CargoTrackerInfrastructureProperties.class})
@RequiredArgsConstructor
class CargoTrackerInfrastructureConfigure {
    private final  CargoTrackerInfrastructureProperties properties;

    @Bean
    public SampleDataInitializer sampleDataInitializer(LocationRepository locationRepository, VoyageRepository voyageRepository) {
        if(properties.isInitializeSample()){
            return new SampleDataInitializer(locationRepository, voyageRepository);
        }
        return null;
    }

}
