package se.citerus.dddsample.application.events;

import lombok.*;
import se.citerus.dddsample.application.shared.ApplicationEvent;

@Setter
@Getter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@EqualsAndHashCode
public class CargoArrivedEvent implements ApplicationEvent {
    private String trackingId;
}
