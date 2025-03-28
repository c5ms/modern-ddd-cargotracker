package com.github.c5ms.cargotracker.domain.model.voyage;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import com.github.c5ms.cargotracker.domain.model.location.Location;
import com.github.c5ms.cargotracker.domain.shared.DomainValue;

import java.time.Instant;


/**
 * A carrier movement is a vessel voyage from one location to another.
 */
@Entity(name = "CarrierMovement")
@Table(name = "CarrierMovement")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class CarrierMovement implements DomainValue<CarrierMovement> {

    // Null object pattern
    @Transient
    public static final CarrierMovement NONE = new CarrierMovement(
        Location.UNKNOWN,
        Location.UNKNOWN,
        Instant.ofEpochMilli(0),
        Instant.ofEpochMilli(0)
    );

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "arrival_location_id", nullable = false)
    public Location arrivalLocation;

    @Column(name = "arrival_time", nullable = false)
    public Instant arrivalTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "departure_location_id", nullable = false)
    public Location departureLocation;

    @Column(name = "departure_time", nullable = false)
    public Instant departureTime;

    /**
     * Constructor.
     *
     * @param departureLocation location of departure
     * @param arrivalLocation   location of arrival
     * @param departureTime     time of departure
     * @param arrivalTime       time of arrival
     */
    private CarrierMovement(Location departureLocation,
                            Location arrivalLocation,
                            Instant departureTime,
                            Instant arrivalTime) {
        //noinspection ObviousNullCheck
        Validate.noNullElements(new Object[]{departureLocation, arrivalLocation, departureTime, arrivalTime});
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.departureLocation = departureLocation;
        this.arrivalLocation = arrivalLocation;
    }

    public static CarrierMovement of(Location departureLocation,
                                     Location arrivalLocation,
                                     Instant departureTime,
                                     Instant arrivalTime) {
        return new CarrierMovement(departureLocation, arrivalLocation, departureTime, arrivalTime);
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final CarrierMovement that = (CarrierMovement) o;

        return sameValueAs(that);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().
            append(this.departureLocation).
            append(this.departureTime).
            append(this.arrivalLocation).
            append(this.arrivalTime).
            toHashCode();
    }

    @Override
    public boolean sameValueAs(CarrierMovement other) {
        return other != null && new EqualsBuilder().
            append(this.departureLocation, other.departureLocation).
            append(this.departureTime, other.departureTime).
            append(this.arrivalLocation, other.arrivalLocation).
            append(this.arrivalTime, other.arrivalTime).
            isEquals();
    }

}
