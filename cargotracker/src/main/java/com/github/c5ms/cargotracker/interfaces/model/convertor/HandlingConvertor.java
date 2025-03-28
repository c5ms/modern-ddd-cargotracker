package com.github.c5ms.cargotracker.interfaces.model.convertor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.github.c5ms.cargotracker.application.command.HandlingReportReceiveCommand;
import com.github.c5ms.cargotracker.domain.model.cargo.TrackingId;
import com.github.c5ms.cargotracker.domain.model.handling.HandlingEvent;
import com.github.c5ms.cargotracker.domain.model.handling.HandlingReport;
import com.github.c5ms.cargotracker.domain.model.location.UnLocode;
import com.github.c5ms.cargotracker.domain.model.voyage.VoyageNumber;
import com.github.c5ms.cargotracker.interfaces.model.dto.HandlingReportDto;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class HandlingConvertor {

    public HandlingReportReceiveCommand toCommand(HandlingReportDto report) {
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
        var reports = trackingIds.stream()
            .map(builder::trackingId)
            .map(HandlingReport.HandlingReportBuilder::build)
            .toList();
        return HandlingReportReceiveCommand.builder()
            .reports(reports)
            .build();

    }
}
