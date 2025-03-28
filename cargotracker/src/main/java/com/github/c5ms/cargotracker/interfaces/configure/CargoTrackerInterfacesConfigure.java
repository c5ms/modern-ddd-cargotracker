package com.github.c5ms.cargotracker.interfaces.configure;

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
import com.github.c5ms.cargotracker.infrastructure.configure.CargoTrackerJacksonProperties;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;


@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({CargoTrackerInterfacesProperties.class, CargoTrackerJacksonProperties.class })
@RequiredArgsConstructor
public class CargoTrackerInterfacesConfigure {

    private final CargoTrackerInterfacesProperties properties;

    public final static String yearMonthFormat = "yyyy-MM";
    public final static String dateFormat = "yyyy-MM-dd";
    public final static String timeFormat = "HH:mm:ss";
    public final static String datetimeFormat = dateFormat + " " + timeFormat;
    public final static TimeZone timeZone = TimeZone.getDefault();

    @Bean
    GroupedOpenApi restApi() {
        return GroupedOpenApi.builder()
            .group("resource-api")
            .displayName("resource")
            .pathsToMatch("/**")
            .build();
    }


    @Bean
    OpenAPI openAPI() {

        SpringDocUtils.getConfig().replaceWithSchema(LocalDate.class, new Schema<LocalDate>()
            .type("string")
            .format(dateFormat)
            .example(LocalDate.of(2024, 1, 1).format(DateTimeFormatter.ofPattern(dateFormat))));

        SpringDocUtils.getConfig().replaceWithSchema(LocalTime.class, new Schema<LocalTime>()
            .type("string")
            .format(timeFormat)
            .example(LocalTime.of(20, 0, 0).format(DateTimeFormatter.ofPattern(timeFormat))));

        SpringDocUtils.getConfig().replaceWithSchema(LocalDateTime.class, new Schema<LocalDateTime>()
            .type("string")
            .format(datetimeFormat)
            .example(LocalDateTime.of(2024, 1, 1, 20, 0, 0).format(DateTimeFormatter.ofPattern(datetimeFormat))));

        SpringDocUtils.getConfig().replaceWithSchema(YearMonth.class, new Schema<YearMonth>()
            .type("string")
            .format(yearMonthFormat)
            .example(YearMonth.of(2024, 1).format(DateTimeFormatter.ofPattern(yearMonthFormat))));

        var openApiProperties = properties.getOpenapi();

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
