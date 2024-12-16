package se.citerus.dddsample.domain.model.voyage;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.Validate;
import se.citerus.dddsample.domain.shared.DomainEntity;

import java.util.List;

/**
 * A Voyage.
 */
@Entity(name = "Voyage")
@Table(name = "Voyage")
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Voyage implements DomainEntity<Voyage> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Embedded
    private VoyageNumber voyageNumber;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "voyage_id")
    private List<CarrierMovement> carrierMovements;

    private Voyage(final VoyageNumber voyageNumber, final Schedule schedule) {
        Validate.notNull(voyageNumber, "Voyage number is required");
        Validate.notNull(schedule, "Schedule is required");

        this.voyageNumber = voyageNumber;
        this.carrierMovements = schedule.carrierMovements().toList();
    }

    public static Voyage of(final VoyageNumber voyageNumber, final Schedule schedule){
        return new Voyage(voyageNumber, schedule);
    }

    public static Voyage empty(){
        return new Voyage(VoyageNumber.empty(), Schedule.empty());
    }

    @Override
    public boolean sameIdentityAs(Voyage other) {
        if(null==other){
            return false;
        }
        return this.getVoyageNumber().equals(other.getVoyageNumber());
    }

}
