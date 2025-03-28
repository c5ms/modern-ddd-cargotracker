package com.github.c5ms.cargotracker.infrastructure.configure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "cargotracker.infrastructure")
public class CargoTrackerJacksonProperties {

}
