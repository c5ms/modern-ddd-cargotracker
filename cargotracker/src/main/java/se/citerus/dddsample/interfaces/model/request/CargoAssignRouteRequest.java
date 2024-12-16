package se.citerus.dddsample.interfaces.model.request;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import se.citerus.dddsample.domain.model.location.UnLocode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@Schema(name = "CargoAssignRouteRequest")
public class CargoAssignRouteRequest {

    private List<Leg> legs = new ArrayList<>();

    @Hidden
    @Getter
    @Setter
    @Accessors(chain = true)
    public static class Leg {

        @Schema(example = "0200T")
        private String voyageNumber;

        @NotNull
        @Pattern(regexp = UnLocode.UN_LOCODE_REGEX)
        @Schema(example = "USNYC")
        private String fromUnLocode;

        @NotNull
        @Pattern(regexp = UnLocode.UN_LOCODE_REGEX)
        @Schema(example = "AUMEL")
        private String toUnLocode;

        private LocalDate fromDate;

        private LocalDate toDate;
    }

}
