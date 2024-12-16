package se.citerus.dddsample.domain.model.cargo;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import se.citerus.dddsample.domain.model.handling.HandlingEvent;
import se.citerus.dddsample.domain.model.location.Location;
import se.citerus.dddsample.domain.model.voyage.Voyage;
import se.citerus.dddsample.domain.shared.DomainValue;

/**
 * A handling activity represents how and where a cargo can be handled,
 * and can be used to express predictions about what is expected to
 * happen to a cargo in the future.
 */
@Getter
@Embeddable
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HandlingActivity implements DomainValue<HandlingActivity> {

    // TODO make HandlingActivity a part of HandlingEvent too? There is some overlap.

    @Enumerated(value = EnumType.STRING)
    @Column(name = "next_expected_handling_event_type")
    private HandlingEvent.Type type;

    @ManyToOne()
    @JoinColumn(name = "next_expected_location_id")
    private Location location;

    @ManyToOne
    @JoinColumn(name = "next_expected_voyage_id")
    private Voyage voyage;

    public HandlingActivity(final HandlingEvent.Type type, final Location location) {
        Validate.notNull(type, "Handling event type is required");
        Validate.notNull(location, "Location is required");

        this.type = type;
        this.location = location;
    }

    public HandlingActivity(final HandlingEvent.Type type, final Location location, final Voyage voyage) {
        Validate.notNull(type, "Handling event type is required");
        Validate.notNull(location, "Location is required");
        Validate.notNull(location, "Voyage is required");

        this.type = type;
        this.location = location;
        this.voyage = voyage;
    }

    @Override
    public boolean sameValueAs(final HandlingActivity other) {
        return other != null && new EqualsBuilder().
            append(this.type, other.type).
            append(this.location, other.location).
            append(this.voyage, other.voyage).
            isEquals();
    }

}
