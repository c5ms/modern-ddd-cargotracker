package se.citerus.dddsample.application.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import se.citerus.dddsample.application.configure.CargoTrackerApplicationConfigure;
import se.citerus.dddsample.application.utils.TestApplicationEvent;
import se.citerus.dddsample.application.utils.TestEventListener;
import se.citerus.dddsample.application.service.HandlingReportProcessor;
import se.citerus.dddsample.application.shared.ApplicationEvent;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@SpringBootTest(classes = {
    TestEventListener.class,
    HandlingReportProcessor.class,
    CargoTrackerApplicationConfigure.class,
})
class DirectlyApplicationEventMessageSenderTest {

    @Autowired
    ApplicationEventMessageSender applicationEventMessageSender;

    @MockitoBean
    HandlingReportProcessor processor;

    @MockitoBean
    TestEventListener testEventListener;

    @Test
    void send() {
        applicationEventMessageSender.send(new TestApplicationEvent());
        then(testEventListener).should(times(1)).on((ApplicationEvent) assertArg(t -> {
            assertThat(t).isInstanceOf(TestApplicationEvent.class);
        }));
    }

}