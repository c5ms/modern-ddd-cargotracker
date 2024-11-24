package se.citerus.dddsample.interfaces.restful.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import se.citerus.dddsample.application.service.HandlingEventReceiver;
import se.citerus.dddsample.interfaces.model.convertor.HandlingConvertor;
import se.citerus.dddsample.interfaces.model.dto.HandlingReportDto;

@Slf4j
@Tag(name = "report")
@RestController
@RequiredArgsConstructor
@RequestMapping("/reports")
public class ReportResource {

    private final HandlingConvertor handlingConvertor;
    private final HandlingEventReceiver handlingEventReceiver;

    @Operation(summary = "receive handing event reports")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping(value = "/reports")
    public void receiveReport(@Validated @RequestBody HandlingReportDto report) {
        var command = handlingConvertor.toReport(report);
        handlingEventReceiver.receiveHandlingReport(command);
    }
}
