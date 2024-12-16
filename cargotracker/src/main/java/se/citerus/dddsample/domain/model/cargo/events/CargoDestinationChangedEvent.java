package se.citerus.dddsample.domain.model.cargo.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import se.citerus.dddsample.domain.model.cargo.Cargo;

@Getter
@Setter
@ToString
@AllArgsConstructor(staticName = "of")
public class CargoDestinationChangedEvent implements CargoEvent {
    private Cargo cargo;
}
