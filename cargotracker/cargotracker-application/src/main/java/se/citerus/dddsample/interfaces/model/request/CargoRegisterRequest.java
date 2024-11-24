package se.citerus.dddsample.interfaces.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import se.citerus.dddsample.domain.model.location.UnLocode;

import java.time.LocalDate;

@Getter
@Setter
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
