package com.github.c5ms.cargotracker.interfaces.restful.resource;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.github.c5ms.cargotracker.application.service.HandlingReportReceiver;
import com.github.c5ms.cargotracker.infrastructure.configure.CargoTrackerJacksonConfigure;
import com.github.c5ms.cargotracker.interfaces.configure.CargoTrackerInterfacesConfigure;
import com.github.c5ms.cargotracker.interfaces.model.convertor.HandlingConvertor;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@WebMvcTest(ReportResource.class)
@Import({
		CargoTrackerInterfacesConfigure.class,
		CargoTrackerJacksonConfigure.class
})
@Tag("report")
@Tag("interfaces")
class ReportResourceTest {

	@Autowired
	MockMvcTester mvc;

	@SuppressWarnings("unused")
	@MockitoBean
	HandlingReportReceiver handlingReportReceiver;

	@MockitoBean
	HandlingConvertor HandlingConvertor;

	@Test
	void list_shouldReturnAccepted() {
		// language=json
		var request = """
				{
				  "completionTime":"2024-12-08 13:39:44",
				  "trackingIds":["1","2"],
				  "type":"LOAD",
				  "unLocode":"DEHAM",
				  "voyageNumber":"CM006"
				}
				""";

		assertThat(this.mvc.post()
				.uri("/reports")
				.content(request)
				.contentType(MediaType.APPLICATION_JSON))
				.hasStatus(HttpStatus.ACCEPTED);

		then(handlingReportReceiver).should(times(1)).receiveHandlingReport(any());
	}


}