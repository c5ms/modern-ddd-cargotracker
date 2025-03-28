package com.github.c5ms.cargotracker.application.command;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import com.github.c5ms.cargotracker.domain.model.handling.HandlingReport;

import java.util.List;


@Getter
@Builder
@RequiredArgsConstructor
public class HandlingReportReceiveCommand {

    private final List<HandlingReport> reports;

}
