package com.github.c5ms.cargotracker.domain.model.handling.support;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.github.c5ms.cargotracker.domain.model.cargo.TrackingId;
import com.github.c5ms.cargotracker.domain.model.handling.HandlingEventFinder;
import com.github.c5ms.cargotracker.domain.model.handling.HandlingEventRepository;
import com.github.c5ms.cargotracker.domain.model.handling.HandlingHistory;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultHandlingEventFinder implements HandlingEventFinder {
    private final HandlingEventRepository handlingEventRepository;

    @Override
    public HandlingHistory handlingHistoryOf(TrackingId trackingId) {
        var events = handlingEventRepository.findByTrackingId(trackingId.getId());
        return HandlingHistory.of(events);
    }

}
