package com.github.c5ms.cargotracker.application.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.github.c5ms.cargotracker.application.configure.CargoTrackerApplicationConfigure;
import com.github.c5ms.cargotracker.application.service.HandlingReportProcessor;
import com.github.c5ms.cargotracker.application.shared.ApplicationEvent;
import com.github.c5ms.cargotracker.application.utils.TestApplicationEvent;
import com.github.c5ms.cargotracker.application.utils.TestEventListener;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

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