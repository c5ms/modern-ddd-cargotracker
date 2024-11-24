package se.citerus.dddsample.application.configure;

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
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import se.citerus.dddsample.application.service.HandlingReportReceiver;
import se.citerus.dddsample.application.service.impl.HandlingReportMessageSender;
import se.citerus.dddsample.application.service.impl.QueuedHandlingReportReceiver;
import se.citerus.dddsample.application.service.impl.ThreadPooledHandlingReportReceiver;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;


@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({CargoTrackerApplicationProperties.class})
@RequiredArgsConstructor
class CargoTrackerApplicationConfigure {

    private final CargoTrackerApplicationProperties cargoTrackerApplicationProperties;

    @Bean
    Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        var properties = cargoTrackerApplicationProperties.getJson();

        return builder -> builder
            .serializerByType(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(properties.getDateFormat())))
            .serializerByType(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(properties.getTimeFormat())))
            .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(properties.getDatetimeFormat())))
            .serializerByType(YearMonth.class, new YearMonthSerializer(DateTimeFormatter.ofPattern(properties.getYearMonthFormat())))

            .deserializerByType(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(properties.getDateFormat())))
            .deserializerByType(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(properties.getTimeFormat())))
            .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(properties.getDatetimeFormat())))
            .deserializerByType(YearMonth.class, new YearMonthDeserializer(DateTimeFormatter.ofPattern(properties.getYearMonthFormat())))

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
    HandlingReportReceiver handlingEventReceiver(ApplicationContext applicationContext) {
        switch (cargoTrackerApplicationProperties.getHandlingEvent().getStrategy()){
            case QUEUED -> {
                return new QueuedHandlingReportReceiver(
                    applicationContext.getBean(HandlingReportMessageSender.class)
                );
            }
            case THREAD_POOLED -> {
                return new ThreadPooledHandlingReportReceiver(
                    applicationContext.getBean(TaskExecutor.class),
                    applicationContext.getBean(ApplicationEventPublisher.class)
                );
            }
        }
        throw new IllegalStateException("unknown how to config HandlingEventReceiver");
    }

}
