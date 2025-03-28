package com.github.c5ms.cargotracker.infrastructure.configure;

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
import com.github.c5ms.cargotracker.interfaces.configure.CargoTrackerInterfacesConfigure;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;


@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({CargoTrackerJacksonProperties.class})
@RequiredArgsConstructor
public class CargoTrackerJacksonConfigure {

    private final static String yearMonthFormat = CargoTrackerInterfacesConfigure.yearMonthFormat;
    private final static String dateFormat = CargoTrackerInterfacesConfigure.dateFormat;
    private final static String timeFormat = CargoTrackerInterfacesConfigure.timeFormat;
    private final static String datetimeFormat = CargoTrackerInterfacesConfigure.datetimeFormat;
    private final static TimeZone timeZone = CargoTrackerInterfacesConfigure.timeZone;

    @Bean
    Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {

        return builder -> builder
            .serializerByType(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(dateFormat)))
            .serializerByType(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(timeFormat)))
            .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(datetimeFormat)))
            .serializerByType(YearMonth.class, new YearMonthSerializer(DateTimeFormatter.ofPattern(yearMonthFormat)))

            .deserializerByType(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(dateFormat)))
            .deserializerByType(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(timeFormat)))
            .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(datetimeFormat)))
            .deserializerByType(YearMonth.class, new YearMonthDeserializer(DateTimeFormatter.ofPattern(yearMonthFormat)))

            .timeZone(timeZone)
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
