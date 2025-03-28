package com.github.c5ms.cargotracker.domain.model.cargo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import com.github.c5ms.cargotracker.domain.shared.DomainValue;

/**
 * Uniquely identifies a particular cargo. Automatically generated by the application.
 */
@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class TrackingId implements DomainValue<TrackingId> {

    @Column(name = "tracking_id", unique = true, updatable = false)
    @NotEmpty(message = "Tracking ID cannot be empty.")
    private String id;

    private TrackingId(@NonNull String id) {
        this.id = id;
    }

    public static TrackingId of(String id) {
        return new TrackingId(id);
    }

    @Override
    public boolean sameValueAs(TrackingId other) {
        return other != null && this.id.equals(other.id);
    }

}
