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
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.citerus.dddsample.domain.model.location.LocationRepository;
import se.citerus.dddsample.domain.model.voyage.VoyageRepository;
import se.citerus.dddsample.infrastructure.initialize.SampleDataInitializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;


@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({CargoTrackerInfrastructureProperties.class})
@RequiredArgsConstructor
class CargoTrackerInfrastructureConfigure {
    private final CargoTrackerInfrastructureProperties properties;


    @Bean
    Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        var propertiesJson = properties.getJson();

        return builder -> builder
            .serializerByType(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(propertiesJson.getDateFormat())))
            .serializerByType(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(propertiesJson.getTimeFormat())))
            .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(propertiesJson.getDatetimeFormat())))
            .serializerByType(YearMonth.class, new YearMonthSerializer(DateTimeFormatter.ofPattern(propertiesJson.getYearMonthFormat())))

            .deserializerByType(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(propertiesJson.getDateFormat())))
            .deserializerByType(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(propertiesJson.getTimeFormat())))
            .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(propertiesJson.getDatetimeFormat())))
            .deserializerByType(YearMonth.class, new YearMonthDeserializer(DateTimeFormatter.ofPattern(propertiesJson.getYearMonthFormat())))

            .timeZone(propertiesJson.getTimeZone())
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

}
