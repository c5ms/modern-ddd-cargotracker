package com.github.c5ms.cargotracker.interfaces.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.github.c5ms.cargotracker.domain.model.location.UnLocode;

@Getter
@Setter
@Accessors(chain = true)
@Schema(name = "CargoDestinationChangeRequest")
public class CargoDestinationChangeRequest {

    @NotNull
    @Pattern(regexp = UnLocode.UN_LOCODE_REGEX)
    @Schema(example = "FIHEL")
    private String destination;

}
