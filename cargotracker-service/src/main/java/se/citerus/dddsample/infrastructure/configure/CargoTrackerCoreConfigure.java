package se.citerus.dddsample.infrastructure.configure;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.YearMonthDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.YearMonthSerializer;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;


@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({CargoTrackerCoreProperties.class, CargoTrackerRestProperties.class})
@RequiredArgsConstructor
class CargoTrackerCoreConfigure {

    private final CargoTrackerCoreProperties cargoTrackerCoreProperties;
    private final CargoTrackerRestProperties cargoTrackerRestProperties;

    @Bean
    Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        var properties = cargoTrackerCoreProperties.getJson();

        return builder -> builder
            .serializerByType(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(properties.getDateFormat())))
            .serializerByType(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(properties.getTimeFormat())))
            .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(properties.getDatetimeFormat())))
            .serializerByType(YearMonth.class, new YearMonthSerializer(DateTimeFormatter.ofPattern(properties.getYearMonthFormat())))

            .deserializerByType(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(properties.getDateFormat())))
            .deserializerByType(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(properties.getTimeFormat())))
            .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(properties.getDatetimeFormat())))
            .deserializerByType(YearMonth.class, new YearMonthDeserializer(DateTimeFormatter.ofPattern(properties.getYearMonthFormat())))

//                .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)

            .timeZone(properties.getTimeZone())
            .featuresToDisable(
                SerializationFeature.FAIL_ON_EMPTY_BEANS,
                SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
            )
            .featuresToEnable(

                JsonParser.Feature.ALLOW_SINGLE_QUOTES,
                JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES,
                SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS,
                MapperFeature.SORT_CREATOR_PROPERTIES_FIRST,
                JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(),
                JsonReadFeature.ALLOW_SINGLE_QUOTES.mappedFeature(),
                JsonReadFeature.ALLOW_UNQUOTED_FIELD_NAMES.mappedFeature()
            )
            .modules(
                new Jdk8Module(),
                new JavaTimeModule(),
                new Hibernate6Module()
                    .enable(Hibernate6Module.Feature.FORCE_LAZY_LOADING)
            );
    }


    @Bean
    public GroupedOpenApi restApi() {
        return GroupedOpenApi.builder()
            .group("resource-api")
            .displayName("resource")
            .pathsToMatch("/**")
            .build();
    }

    @Bean
    public OpenAPI openAPI() {
        var jsonProperties = cargoTrackerCoreProperties.getJson();


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

        var openApiProperties = cargoTrackerRestProperties.getOpenapi();

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
