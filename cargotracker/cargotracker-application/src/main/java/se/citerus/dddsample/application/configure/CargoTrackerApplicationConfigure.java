package se.citerus.dddsample.application.configure;

import lombok.RequiredArgsConstructor;
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


@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({CargoTrackerApplicationProperties.class})
@RequiredArgsConstructor
class CargoTrackerApplicationConfigure {

    private final CargoTrackerApplicationProperties cargoTrackerApplicationProperties;

    @Bean
    HandlingReportReceiver handlingEventReceiver(ApplicationContext applicationContext) {
        switch (cargoTrackerApplicationProperties.getHandlingEvent().getStrategy()) {
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
