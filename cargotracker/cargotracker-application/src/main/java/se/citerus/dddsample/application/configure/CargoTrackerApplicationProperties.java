package se.citerus.dddsample.application.configure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.TimeZone;

@Data
@ConfigurationProperties(prefix = "cargotracker.application")
public class CargoTrackerApplicationProperties {

    private final Json json = new Json();

    private final HandlingEvent handlingEvent = new HandlingEvent();

    @Data
    public static class HandlingEvent {
        private HandlingEventProcessStrategy strategy;
    }

    public enum HandlingEventProcessStrategy {
        QUEUED,
        THREAD_POOLED
    }

    @Data
    public static class Json {
        private String yearMonthFormat = "yyyy-MM";

        private String dateFormat = "yyyy-MM-dd";

        private String timeFormat = "HH:mm:ss";

        private String datetimeFormat = dateFormat + " " + timeFormat;

        private TimeZone timeZone = TimeZone.getDefault();
    }
}
