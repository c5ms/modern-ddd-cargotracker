package se.citerus.dddsample.application.command;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import se.citerus.dddsample.domain.model.handling.HandlingReport;

import java.util.List;


@Getter
@Builder
@RequiredArgsConstructor
public class HandlingReportReceiveCommand {

    private final List<HandlingReport> reports;

}
