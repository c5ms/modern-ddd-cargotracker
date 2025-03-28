package com.github.c5ms.cargotracker.application.events;

import lombok.*;
import com.github.c5ms.cargotracker.application.shared.ApplicationEvent;

@Setter
@Getter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@EqualsAndHashCode
public class CargoHandledEvent implements ApplicationEvent {
    private String trackingId;
}
