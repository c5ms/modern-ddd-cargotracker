package se.citerus.dddsample.interfaces.restful;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import se.citerus.dddsample.application.service.HandlingReportReceiver;
import se.citerus.dddsample.domain.model.handling.HandlingEvent;
import se.citerus.dddsample.infrastructure.initialize.SampleLocations;
import se.citerus.dddsample.infrastructure.initialize.SampleVoyages;
import se.citerus.dddsample.interfaces.model.dto.HandlingReportDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ReportResourceTest {

    @Autowired
    MockMvc mockMvc;

    @SuppressWarnings("unused")
    @MockitoBean
    HandlingReportReceiver handlingReportReceiver;

    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @Test
    void list_shouldReturnAccepted() throws Exception {
        var dto = new HandlingReportDto()
            .setType(HandlingEvent.Type.LOAD.name())
            .setUnLocode(SampleLocations.HAMBURG.getUnlocode())
            .setVoyageNumber(SampleVoyages.CM006.getVoyageNumber())
            .setCompletionTime(LocalDateTime.now())
            .setTrackingIds(List.of("1", "2"));
        this.mockMvc.perform(
            post("/reports")
                .content(jacksonObjectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isAccepted());
    }


}