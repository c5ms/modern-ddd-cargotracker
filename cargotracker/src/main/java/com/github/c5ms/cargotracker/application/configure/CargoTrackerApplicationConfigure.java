package com.github.c5ms.cargotracker.application.configure;

import com.github.c5ms.cargotracker.application.service.impl.ApplicationEventMessageSender;
import com.github.c5ms.cargotracker.application.service.impl.DirectlyApplicationEventMessageSender;
import com.github.c5ms.cargotracker.application.service.impl.DirectlyHandlingReportReceiver;
import com.github.c5ms.cargotracker.application.service.impl.HandlingReportMessageSender;
import com.github.c5ms.cargotracker.application.service.impl.MessageHandlingReportReceiver;
import com.github.c5ms.cargotracker.application.service.impl.ThreadPooledHandlingReportReceiver;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import com.github.c5ms.cargotracker.application.service.HandlingReportProcessor;
import com.github.c5ms.cargotracker.application.service.HandlingReportReceiver;


@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({CargoTrackerApplicationProperties.class})
@RequiredArgsConstructor
public class CargoTrackerApplicationConfigure {

    @Bean
    @ConditionalOnProperty(prefix = "cargotracker.application", name = "handing-report.process-strategy", havingValue = "THREAD")
    HandlingReportReceiver threadPooledHandlingReportReceiver(TaskExecutor taskExecutor, HandlingReportProcessor processor) {
        return new ThreadPooledHandlingReportReceiver(taskExecutor, processor);
    }

    @Bean
    @ConditionalOnProperty(prefix = "cargotracker.application", name = "handing-report.process-strategy", havingValue = "MESSAGE")
    HandlingReportReceiver queuedHandlingReportReceiver(HandlingReportMessageSender sender) {
        return new MessageHandlingReportReceiver(sender);
    }

    @Bean
    @ConditionalOnProperty(prefix = "cargotracker.application", name = "handing-report.process-strategy", havingValue = "DIRECT")
    HandlingReportReceiver internalReportReceiver(HandlingReportProcessor handlingReportProcessor) {
        return new DirectlyHandlingReportReceiver(handlingReportProcessor);
    }

    @Bean
    @ConditionalOnMissingBean(ApplicationEventMessageSender.class)
    ApplicationEventMessageSender applicationEventMessageSender(ApplicationEventPublisher publisher) {
        return new DirectlyApplicationEventMessageSender(publisher);
    }

}
