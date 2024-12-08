package se.citerus.dddsample.interfaces.configure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "cargotracker.interfaces")
public class CargoTrackerInterfacesProperties {

    private final ElevenOpenapiProperties openapi = new ElevenOpenapiProperties();

    @Data
    public static class ElevenOpenapiProperties {
        private String title;
        private String version;
        private String description;
        private String termsOfService;

        private Contact contact=new Contact();
        private License license= new License();

        @Data
        public static class Contact {
            private String name;
            private String url;
            private String email;
        }

        @Data
        public static class License {
            private String name;
            private String url;
        }
    }
}
