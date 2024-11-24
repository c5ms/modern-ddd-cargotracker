package se.citerus.dddsample.application.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import se.citerus.dddsample.application.shared.ApplicationEvent;

@Setter
@Getter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class CargoArrivedEvent implements ApplicationEvent {
    private String trackingId;
}
