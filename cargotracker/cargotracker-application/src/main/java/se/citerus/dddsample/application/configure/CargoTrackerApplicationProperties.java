package se.citerus.dddsample.application.configure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "cargotracker.application")
public class CargoTrackerApplicationProperties {

    private final HandlingReport handingReport = new HandlingReport();

    public enum HandlingReportProcessStrategy {
        MESSAGE,
        THREAD,
        /**
         * for test
         */
        INTERNAL
    }

    @Data
    public static class HandlingReport {
        private HandlingReportProcessStrategy processStrategy = HandlingReportProcessStrategy.INTERNAL;
    }

}
