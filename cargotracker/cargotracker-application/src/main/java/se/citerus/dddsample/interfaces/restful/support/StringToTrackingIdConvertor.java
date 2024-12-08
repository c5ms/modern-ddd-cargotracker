package se.citerus.dddsample.interfaces.restful.support;

import lombok.NonNull;
import org.apache.commons.lang3.Validate;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import se.citerus.dddsample.domain.model.cargo.TrackingId;

import java.util.Objects;

@Component
public class StringToTrackingIdConvertor implements Converter<String, TrackingId> {
    @Override
    public TrackingId convert(@NonNull String source) {
        Validate.notEmpty(source);
        return TrackingId.of(source);
    }
}
