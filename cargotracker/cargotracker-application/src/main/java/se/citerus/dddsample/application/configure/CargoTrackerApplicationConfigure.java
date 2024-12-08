package se.citerus.dddsample.application.configure;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import se.citerus.dddsample.application.service.HandlingReportProcessor;
import se.citerus.dddsample.application.service.HandlingReportHandler;
import se.citerus.dddsample.application.service.impl.*;


@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({CargoTrackerApplicationProperties.class})
@RequiredArgsConstructor
public class CargoTrackerApplicationConfigure {

    @Bean
    @ConditionalOnProperty(prefix = "cargotracker.application", name = "handing-report.process-strategy", havingValue = "THREAD")
    HandlingReportHandler threadPooledHandlingReportReceiver(TaskExecutor taskExecutor, HandlingReportProcessor processor) {
        return new ThreadPooledHandlingReportHandler(taskExecutor, processor);
    }

    @Bean
    @ConditionalOnProperty(prefix = "cargotracker.application", name = "handing-report.process-strategy", havingValue = "MESSAGE")
    HandlingReportHandler queuedHandlingReportReceiver(HandlingReportMessageSender sender) {
        return new MessageHandlingReportHandler(sender);
    }

    @Bean
    @ConditionalOnProperty(prefix = "cargotracker.application", name = "handing-report.process-strategy", havingValue = "DIRECT")
    HandlingReportHandler internalReportReceiver(HandlingReportProcessor handlingReportProcessor) {
        return new DirectlyHandlingReportHandler(handlingReportProcessor);
    }

    @Bean
    @ConditionalOnMissingBean(ApplicationEventMessageSender.class)
    ApplicationEventMessageSender applicationEventMessageSender(ApplicationEventPublisher publisher) {
        return new DirectlyApplicationEventMessageSender(publisher);
    }

}
