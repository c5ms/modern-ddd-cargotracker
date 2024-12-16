package se.citerus.dddsample.application.command;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import se.citerus.dddsample.domain.model.location.UnLocode;

@Getter
@Builder
@RequiredArgsConstructor
public class CargoDestinationChangeCommand {
    private final UnLocode destination;
}
