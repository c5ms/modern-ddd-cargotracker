package se.citerus.dddsample.interfaces.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@Schema(name = "Location")
@Accessors(chain = true)
public class LegDto implements Serializable {
    private String voyageNumber;
    private String from;
    private String to;
    private Instant loadTime;
    private Instant unloadTime;
}