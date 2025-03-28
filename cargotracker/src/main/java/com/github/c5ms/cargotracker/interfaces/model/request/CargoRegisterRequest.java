package com.github.c5ms.cargotracker.interfaces.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.github.c5ms.cargotracker.domain.model.location.UnLocode;

import java.time.LocalDate;

@Getter
@Setter
@Accessors(chain = true)
@Schema(name = "CargoRegisterRequest")
public class CargoRegisterRequest {

    @NotNull
    @Pattern(regexp = UnLocode.UN_LOCODE_REGEX)
    @Schema(example = "CNHKG")
    private String originUnlocode;

    @NotNull
    @Pattern(regexp = UnLocode.UN_LOCODE_REGEX)
    @Schema(example = "FIHEL")
    private String destinationUnlocode;

    @NotNull
    @Schema(example = "2026-10-01")
    private LocalDate arrivalDeadline;

}
