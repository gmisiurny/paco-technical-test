package technical.test.api.representation;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record PostFlightRequest(

    @NotNull(message = "La date de départ ne peut pas être nulle")
    @Future(message = "La date de départ doit être dans le futur")
    LocalDateTime departure,

    @NotNull(message = "La date d'arrivée ne peut pas être nulle")
    @Future(message = "La date d'arrivée doit être dans le futur")
    LocalDateTime arrival,

    @Positive
    double price,

    @Pattern(regexp = "^[A-Z]{3}$")
    String originId,

    @Pattern(regexp = "^[A-Z]{3}$")
    String destinationId,

    @Pattern(
        regexp = "^https?://[\\w\\-]+(\\.[\\w\\-]+)+([\\w\\-\\.,@?^=%&:/~\\+#]*[\\w\\-\\@?^=%&/~\\+#])?$",
        message = "L'URL de l'image doit être valide (http:// ou https://)"
    )
    String image
) {

    @AssertTrue(message = "La date d'arrivée doit être postérieure à la date de départ")
    public boolean isArrivalAfterDeparture() {
        return this.arrival.isAfter(this.departure);
    }

}
