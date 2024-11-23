package se.citerus.dddsample.application.command;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import se.citerus.dddsample.domain.model.location.UnLocode;

import java.time.Instant;


@Getter
@Builder
@RequiredArgsConstructor
public class CargoRegisterCommand {
    private final UnLocode origin;
    private final UnLocode destination;
    private final Instant arrivalDeadline;
}
