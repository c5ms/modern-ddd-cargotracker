package se.citerus.dddsample.domain.model.location;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.Validate;
import se.citerus.dddsample.domain.shared.DomainEntity;

/**
 * A location is our model is stops on a journey, such as cargo
 * origin or destination, or carrier movement endpoints.
 * It is uniquely identified by a UN Locode.
 */
@Entity(name = "Location")
@Table(name = "Location")
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Location implements DomainEntity<Location> {

    public static final Location UNKNOWN = new Location("XXXXX", "Unknown location");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Embedded
    private UnLocode unlocode;

    @Column(nullable = false)
    private String name;

    /**
     * Package-level constructor, visible for test and sample data purposes.
     *
     * @param unLocode UN Locode
     * @param name     location name
     * @throws IllegalArgumentException if the UN Locode or name is null
     */
    private Location(final UnLocode unLocode, final String name) {
        Validate.notNull(unLocode, "unLocode can not be null");
        Validate.notNull(name, "name can not be null");

        this.unlocode = unLocode;
        this.name = name;
    }

    private Location(String unlocode, String name) {
        this(UnLocode.of(unlocode), name);
    }


    public static Location of(final String unLocode, final String name) {
        return new Location(unLocode, name);
    }

    public static Location of(final UnLocode unLocode, final String name) {
        return new Location(unLocode, name);
    }

    @Override
    public boolean sameIdentityAs(final Location other) {
        if (null == other) {
            return false;
        }
        return this.unlocode.equals(other.unlocode);
    }

}
