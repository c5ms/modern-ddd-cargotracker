package se.citerus.dddsample.infrastructure.configure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.TimeZone;

@Data
@ConfigurationProperties(prefix = "cargotracker.infrastructure")
public class CargoTrackerInfrastructureProperties {

    private final Json json = new Json();

    @Data
    public static class Json {
        private String yearMonthFormat = "yyyy-MM";

        private String dateFormat = "yyyy-MM-dd";

        private String timeFormat = "HH:mm:ss";

        private String datetimeFormat = dateFormat + " " + timeFormat;

        private TimeZone timeZone = TimeZone.getDefault();
    }

}
