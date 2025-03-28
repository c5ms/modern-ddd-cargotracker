package com.github.c5ms.cargotracker.interfaces.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Schema(name = "HandlingReport")
@Accessors(chain = true)
public class HandlingReportDto {

    @NotNull
    @Schema(example = "2022-10-29 13:37")
    private LocalDateTime completionTime;

    @NotNull
    @NotEmpty
    @Schema(example = "[\"ABC123\"]")
    private List<String> trackingIds;

    @NotNull
    @Schema(example = "LOAD")
    private String type;

    @NotNull
    @Schema(example = "SESTO")
    private String unLocode;

    @NotBlank
    @Schema(example = "0200T")
    private String voyageNumber;
}
