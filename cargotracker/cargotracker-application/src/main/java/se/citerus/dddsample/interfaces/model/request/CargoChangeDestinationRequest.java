package se.citerus.dddsample.interfaces.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import se.citerus.dddsample.domain.model.location.UnLocode;

@Getter
@Setter
@Schema(name = "CargoChangeDestinationRequest")
public class CargoChangeDestinationRequest {
    @NotNull
    @Pattern(regexp = UnLocode.UN_LOCODE_REGEX)
    @Schema(example = "FIHEL")
    private String destination;
}
