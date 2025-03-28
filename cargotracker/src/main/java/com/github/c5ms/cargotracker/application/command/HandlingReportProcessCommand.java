package com.github.c5ms.cargotracker.application.command;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import com.github.c5ms.cargotracker.domain.model.handling.HandlingReport;


@Getter
@Builder
@RequiredArgsConstructor
public class HandlingReportProcessCommand {

    private final HandlingReport report;

}
