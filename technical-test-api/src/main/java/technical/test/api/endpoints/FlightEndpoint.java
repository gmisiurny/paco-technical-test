package technical.test.api.endpoints;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import technical.test.api.facade.FlightFacade;
import technical.test.api.record.FlightRecord;
import technical.test.api.representation.FlightRepresentation;
import technical.test.api.representation.PostFlightRequest;

@RestController
@RequestMapping(FlightEndpoint.BASE_PATH)
@RequiredArgsConstructor
public class FlightEndpoint {

    protected static final String BASE_PATH = "/flight";
    private final FlightFacade flightFacade;

    @GetMapping
    public Flux<FlightRepresentation> getAllFlights() {
        return this.flightFacade.getAllFlights();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<FlightRecord> saveFlight(@Valid @RequestBody final PostFlightRequest postFlightRequest) {
        return this.flightFacade.saveFlight(postFlightRequest);
    }

}

