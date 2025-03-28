package com.github.c5ms.cargotracker.interfaces.restful.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.github.c5ms.cargotracker.application.service.HandlingReportReceiver;
import com.github.c5ms.cargotracker.interfaces.model.convertor.HandlingConvertor;
import com.github.c5ms.cargotracker.interfaces.model.dto.HandlingReportDto;

@Slf4j
@Tag(name = "report")
@RestController
@RequiredArgsConstructor
@RequestMapping("/reports")
public class ReportResource {

    private final HandlingConvertor handlingConvertor;
    private final HandlingReportReceiver handlingReportReceiver;

    @Operation(summary = "receive handing event reports")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping
    public void receiveReport(@Validated @RequestBody HandlingReportDto report) {
        var command = handlingConvertor.toCommand(report);
        handlingReportReceiver.receiveHandlingReport(command);
    }
}
