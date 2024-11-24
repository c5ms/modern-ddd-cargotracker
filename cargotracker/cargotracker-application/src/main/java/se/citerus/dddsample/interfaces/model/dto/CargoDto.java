package se.citerus.dddsample.interfaces.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Schema(name = "Cargo")
@Accessors(chain = true)
public class CargoDto implements Serializable {
    private String trackingId;
    private Boolean routed;
    private String origin;
    private String destination;
    private Instant arrivalDeadline;
}