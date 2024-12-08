package se.citerus.dddsample.interfaces.restful.resource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import se.citerus.dddsample.application.service.HandlingReportReceiver;
import se.citerus.dddsample.infrastructure.configure.CargoTrackerJacksonConfigure;
import se.citerus.dddsample.interfaces.configure.CargoTrackerInterfacesConfigure;
import se.citerus.dddsample.interfaces.model.convertor.HandlingConvertor;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@WebMvcTest(ReportResource.class)
@Import({
    CargoTrackerInterfacesConfigure.class,
    CargoTrackerJacksonConfigure.class
})
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