package com.github.c5ms.cargotracker.domain.model.handling;

import com.github.c5ms.cargotracker.domain.model.cargo.TrackingId;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import com.github.c5ms.cargotracker.domain.model.cargo.Cargo;
import com.github.c5ms.cargotracker.domain.model.location.Location;
import com.github.c5ms.cargotracker.domain.model.voyage.Voyage;
import com.github.c5ms.cargotracker.domain.shared.DomainEntity;

import java.time.Instant;

/**
 * A HandlingEvent is used to register the event when, for instance,
 * a cargo is unloaded from a carrier at some location at a given time.
 * <p/>
 * The HandlingEvent's are sent from different Incident Logging Applications
 * some time after the event occurred and contain information about the
 * {@link TrackingId}, {@link Location}, timestamp of the completion of the event,
 * and possibly, if applicable a {@link Voyage}.
 * <p/>
 * This class is the only member, and consequently the root, of the HandlingEvent aggregate.
 * <p/>
 * HandlingEvent's could contain information about a {@link Voyage} and if so,
 * the event type must be either {@link Type#LOAD} or {@link Type#UNLOAD}.
 * <p/>
 * All other events must be of {@link Type#RECEIVE}, {@link Type#CLAIM} or {@link Type#CUSTOMS}.
 */
@Entity(name = "HandlingEvent")
@Table(name = "HandlingEvent")
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public final class HandlingEvent implements DomainEntity<HandlingEvent> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voyage_id")
    public Voyage voyage;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "location_id")
    public Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cargo_id")
    public Cargo cargo;

    @Column
    public Instant completionTime;

    @Column
    public Instant registrationTime;

    @Column
    @Enumerated(value = EnumType.STRING)
    public Type type;

    /**
     * @param cargo            cargo
     * @param completionTime   completion time, the reported time that the event actually happened (e.g. the receive took place).
     * @param registrationTime registration time, the time the message is received
     * @param type             type of event
     * @param location         where the event took place
     * @param voyage           the voyage
     */
    private HandlingEvent(final Cargo cargo,
                          final Instant completionTime,
                          final Instant registrationTime,
                          final Type type,
                          final Location location,
                          final Voyage voyage) {
        Validate.notNull(cargo, "Cargo is required");
        Validate.notNull(completionTime, "Completion time is required");
        Validate.notNull(registrationTime, "Registration time is required");
        Validate.notNull(type, "Handling event type is required");
        Validate.notNull(location, "Location is required");
        Validate.notNull(voyage, "Voyage is required");

        if (type.prohibitsVoyage()) {
            throw new IllegalArgumentException("Voyage is not allowed with event type " + type);
        }

        this.voyage = voyage;
        this.completionTime = completionTime;
        this.registrationTime = registrationTime;
        this.type = type;
        this.location = location;
        this.cargo = cargo;
    }

    /**
     * @param cargo            cargo
     * @param completionTime   completion time, the reported time that the event actually happened (e.g. the receive took place).
     * @param registrationTime registration time, the time the message is received
     * @param type             type of event
     * @param location         where the event took place
     */
    private HandlingEvent(final Cargo cargo,
                          final Instant completionTime,
                          final Instant registrationTime,
                          final Type type,
                          final Location location) {
        Validate.notNull(cargo, "Cargo is required");
        Validate.notNull(completionTime, "Completion time is required");
        Validate.notNull(registrationTime, "Registration time is required");
        Validate.notNull(type, "Handling event type is required");
        Validate.notNull(location, "Location is required");

        if (type.requiresVoyage()) {
            throw new IllegalArgumentException("Voyage is required for event type " + type);
        }

        this.completionTime = completionTime;
        this.registrationTime = registrationTime;
        this.type = type;
        this.location = location;
        this.cargo = cargo;
        this.voyage = null;
    }

    public static HandlingEvent of(final Cargo cargo,
                                   final Instant completionTime,
                                   final Instant registrationTime,
                                   final Type type,
                                   final Location location) {
        return new HandlingEvent(cargo, completionTime, registrationTime, type, location);
    }

    public static HandlingEvent createHandlingEvent(final Cargo cargo,
                                                    final Instant completionTime,
                                                    final Instant registrationTime,
                                                    final Type type,
                                                    final Location location,
                                                    final Voyage voyage) {
        return new HandlingEvent(cargo, completionTime, registrationTime, type, location, voyage);
    }

    public boolean sameEventAs(final HandlingEvent other) {
        return other != null && new EqualsBuilder().
            append(this.cargo, other.cargo).
            append(this.voyage, other.voyage).
            append(this.completionTime, other.completionTime).
            append(this.location, other.location).
            append(this.type, other.type).
            isEquals();
    }


    @Override
    public boolean sameIdentityAs(final HandlingEvent other) {
        if(null==other){
            return false;
        }
        return this.id == other.id;
    }


    /**
     * Handling event type. Either requires or prohibits a carrier movement
     * association, it's never optional.
     */
    public enum Type{
        LOAD(true),
        UNLOAD(true),
        RECEIVE(false),
        CLAIM(false),
        CUSTOMS(false);

        private final boolean voyageRequired;

        /**
         * Private enum constructor.
         *
         * @param voyageRequired whether or not a voyage is associated with this event type
         */
        Type(final boolean voyageRequired) {
            this.voyageRequired = voyageRequired;
        }

        /**
         * @return True if a voyage association is required for this event type.
         */
        public boolean requiresVoyage() {
            return voyageRequired;
        }

        /**
         * @return True if a voyage association is prohibited for this event type.
         */
        public boolean prohibitsVoyage() {
            return !requiresVoyage();
        }
    }


}
