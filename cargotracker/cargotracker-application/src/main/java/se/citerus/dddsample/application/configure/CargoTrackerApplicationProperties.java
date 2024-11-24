package se.citerus.dddsample.application.configure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "cargotracker.application")
public class CargoTrackerApplicationProperties {

    private final HandlingEvent handlingEvent = new HandlingEvent();

    public enum HandlingEventProcessStrategy {
        QUEUED,
        THREAD_POOLED
    }

    @Data
    public static class HandlingEvent {
        private HandlingEventProcessStrategy strategy;
    }

}
