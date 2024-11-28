package se.citerus.dddsample.domain.model.cargo;

import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import se.citerus.dddsample.domain.model.location.Location;
import se.citerus.dddsample.domain.model.voyage.Voyage;
import se.citerus.dddsample.domain.shared.ValueObject;

import java.time.Instant;

/**
 * An itinerary consists of one or more legs.
 */
@Entity(name = "Leg")
@Table(name = "Leg")
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Leg implements ValueObject<Leg> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "voyage_id")
    private Voyage voyage;

    @ManyToOne
    @JoinColumn(name = "load_location_id")
    private Location loadLocation;

    @Column(name = "load_time")
    private Instant loadTime;

    @ManyToOne
    @JoinColumn(name = "unload_location_id")
    private Location unloadLocation;

    @Column(name = "unload_time")
    private Instant unloadTime;

    private Leg(Voyage voyage, Location loadLocation, Location unloadLocation, Instant loadTime, Instant unloadTime) {
        Validate.notNull(voyage, "voyage must not be null");
        Validate.notNull(loadLocation, "loadLocation must not be null");
        Validate.notNull(unloadLocation, "unloadLocation must not be null");
        Validate.notNull(loadTime, "loadTime must not be null");
        Validate.notNull(unloadTime, "unloadTime must not be null");

        this.voyage = voyage;
        this.loadLocation = loadLocation;
        this.unloadLocation = unloadLocation;
        this.loadTime = loadTime;
        this.unloadTime = unloadTime;
    }

    public static Leg of(Voyage voyage, Location loadLocation, Location unloadLocation, Instant loadTime, Instant unloadTime) {
        return new Leg(voyage, loadLocation, unloadLocation, loadTime, unloadTime);
    }

    @Override
    public boolean sameValueAs(final Leg other) {
        return other != null && new EqualsBuilder().
            append(this.voyage, other.voyage).
            append(this.loadLocation, other.loadLocation).
            append(this.unloadLocation, other.unloadLocation).
            append(this.loadTime, other.loadTime).
            append(this.unloadTime, other.unloadTime).
            isEquals();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Leg leg = (Leg) o;

        return sameValueAs(leg);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().
            append(voyage).
            append(loadLocation).
            append(unloadLocation).
            append(loadTime).
            append(unloadTime).
            toHashCode();
    }

}
