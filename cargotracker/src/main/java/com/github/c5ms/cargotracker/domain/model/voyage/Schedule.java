package com.github.c5ms.cargotracker.domain.model.voyage;

import jakarta.persistence.Embeddable;
import lombok.*;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import com.github.c5ms.cargotracker.domain.shared.ImmutableValues;
import com.github.c5ms.cargotracker.domain.shared.DomainValue;

import java.util.Collections;
import java.util.List;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule implements DomainValue<Schedule> {

    private List<CarrierMovement> carrierMovements = Collections.emptyList();

    private Schedule(@NonNull final List<CarrierMovement> carrierMovements) {
        Validate.noNullElements(carrierMovements);
        Validate.notEmpty(carrierMovements);

        this.carrierMovements = carrierMovements;
    }

    public static Schedule of(List<CarrierMovement> carrierMovements) {
        return new Schedule(carrierMovements);
    }

    public static Schedule empty() {
        return new Schedule();
    }

    /**
     * @return Carrier movements.
     */
    public ImmutableValues<CarrierMovement> carrierMovements() {
        return ImmutableValues.of(carrierMovements);
    }

    @Override
    public boolean sameValueAs(final Schedule other) {
        return other != null && this.carrierMovements.equals(other.carrierMovements);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Schedule that = (Schedule) o;

        return sameValueAs(that);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.carrierMovements).toHashCode();
    }


}
