package se.citerus.dddsample.application.command;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import se.citerus.dddsample.domain.model.handling.HandlingEventReport;


@Getter
@Builder
@RequiredArgsConstructor
public class HandlingReportProcessCommand {

    private final HandlingEventReport report;
    
}
