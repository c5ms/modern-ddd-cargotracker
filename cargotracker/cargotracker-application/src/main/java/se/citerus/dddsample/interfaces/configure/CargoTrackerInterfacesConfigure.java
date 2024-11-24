package se.citerus.dddsample.interfaces.configure;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.citerus.dddsample.infrastructure.configure.CargoTrackerInfrastructureProperties;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;


@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({CargoTrackerInterfacesProperties.class})
@RequiredArgsConstructor
class CargoTrackerInterfacesConfigure {

    private final CargoTrackerInterfacesProperties cargoTrackerInterfacesProperties;

    @Bean
    public GroupedOpenApi restApi() {
        return GroupedOpenApi.builder()
            .group("resource-api")
            .displayName("resource")
            .pathsToMatch("/**")
            .build();
    }

    @Bean
    public OpenAPI openAPI(CargoTrackerInfrastructureProperties infrastructureProperties) {
        var jsonProperties = infrastructureProperties.getJson();


        SpringDocUtils.getConfig().replaceWithSchema(LocalDate.class, new Schema<LocalDate>()
            .type("string")
            .format(jsonProperties.getTimeFormat())
            .example(LocalDate.of(2024, 1, 1).format(DateTimeFormatter.ofPattern(jsonProperties.getDateFormat()))));

        SpringDocUtils.getConfig().replaceWithSchema(LocalTime.class, new Schema<LocalTime>()
            .type("string")
            .format(jsonProperties.getTimeFormat())
            .example(LocalTime.of(20, 0, 0).format(DateTimeFormatter.ofPattern(jsonProperties.getTimeFormat()))));

        SpringDocUtils.getConfig().replaceWithSchema(LocalDateTime.class, new Schema<LocalDateTime>()
            .type("string")
            .format(jsonProperties.getTimeFormat())
            .example(LocalDateTime.of(2024, 1, 1, 20, 0, 0).format(DateTimeFormatter.ofPattern(jsonProperties.getDatetimeFormat()))));

        SpringDocUtils.getConfig().replaceWithSchema(YearMonth.class, new Schema<YearMonth>()
            .type("string")
            .format(jsonProperties.getTimeFormat())
            .example(YearMonth.of(2024, 1).format(DateTimeFormatter.ofPattern(jsonProperties.getYearMonthFormat()))));

        var openApiProperties = cargoTrackerInterfacesProperties.getOpenapi();

        return new OpenAPI()
            .info(new Info()
                .version(openApiProperties.getVersion())
                .title(openApiProperties.getTitle())
                .description(openApiProperties.getDescription())
                .termsOfService(openApiProperties.getTermsOfService())
                .contact(new Contact()
                    .name(openApiProperties.getContact().getName())
                    .url(openApiProperties.getContact().getUrl())
                    .email(openApiProperties.getContact().getEmail()))
                .license(new License()
                    .name(openApiProperties.getLicense().getName())
                    .url(openApiProperties.getLicense().getUrl()))
            );
    }

}
