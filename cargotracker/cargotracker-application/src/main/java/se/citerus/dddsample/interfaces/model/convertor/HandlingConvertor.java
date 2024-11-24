package se.citerus.dddsample.interfaces.model.convertor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import se.citerus.dddsample.application.command.HandlingReportReceiveCommand;
import se.citerus.dddsample.domain.model.handling.HandlingReport;
import se.citerus.dddsample.domain.model.cargo.TrackingId;
import se.citerus.dddsample.domain.model.handling.HandlingEvent;
import se.citerus.dddsample.domain.model.location.UnLocode;
import se.citerus.dddsample.domain.model.voyage.VoyageNumber;
import se.citerus.dddsample.interfaces.model.dto.HandlingReportDto;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class HandlingConvertor {


    public HandlingReportReceiveCommand toReport(HandlingReportDto report) {
        final Instant completionTime = report.getCompletionTime().toInstant(ZoneOffset.UTC);
        final VoyageNumber voyageNumber = Optional.ofNullable(report.getVoyageNumber()).map(VoyageNumber::of).orElse(null);
        final HandlingEvent.Type type = HandlingEvent.Type.valueOf(report.getType());
        final UnLocode unLocode = UnLocode.of(report.getUnLocode());
        final List<TrackingId> trackingIds = report.getTrackingIds().stream().map(TrackingId::of).toList();

        var builder = HandlingReport.builder()
            .registrationTime(Instant.now())
            .completionTime(completionTime)
            .voyageNumber(voyageNumber)
            .type(type)
            .unLocode(unLocode);
         var reports= trackingIds.stream()
            .map(builder::trackingId)
            .map(HandlingReport.HandlingReportBuilder::build)
            .toList();
         return HandlingReportReceiveCommand.builder()
             .reports(reports)
             .build();

    }
}
