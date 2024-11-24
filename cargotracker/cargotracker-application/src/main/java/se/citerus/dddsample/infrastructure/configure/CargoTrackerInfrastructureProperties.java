package se.citerus.dddsample.infrastructure.configure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.TimeZone;

@Data
@ConfigurationProperties(prefix = "cargotracker.infrastructure")
public class CargoTrackerInfrastructureProperties {

    private boolean initializeSample = false;

}
