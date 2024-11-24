package se.citerus.dddsample.interfaces.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import se.citerus.dddsample.domain.model.cargo.HandlingActivity;
import se.citerus.dddsample.domain.model.cargo.RoutingStatus;
import se.citerus.dddsample.domain.model.cargo.TransportStatus;
import se.citerus.dddsample.domain.model.location.Location;

import java.time.Instant;

@Getter
@Setter
@Schema(name = "Delivery")
@Accessors(chain = true)
public class DeliveryDto {

    private boolean misdirected;

    private Instant eta;

    private Instant calculatedAt;

    private boolean isUnloadedAtDestination;

    private RoutingStatus routingStatus;

    private HandlingActivity nextExpectedActivity;

    private TransportStatus transportStatus;

    private String currentVoyageNumber;

    private Location lastKnownLocation;

}
