package com.github.c5ms.cargotracker.interfaces.restful.support;

import lombok.NonNull;
import org.apache.commons.lang3.Validate;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import com.github.c5ms.cargotracker.domain.model.cargo.TrackingId;

@Component
public class StringToTrackingIdConvertor implements Converter<String, TrackingId> {
    @Override
    public TrackingId convert(@NonNull String source) {
        Validate.notEmpty(source);
        return TrackingId.of(source);
    }
}
