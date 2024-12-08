package se.citerus.dddsample.infrastructure.configure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "cargotracker.infrastructure")
public class CargoTrackerJacksonProperties {

}
