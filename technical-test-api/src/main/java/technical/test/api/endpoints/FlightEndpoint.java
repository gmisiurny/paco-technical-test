package technical.test.api.endpoints;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import technical.test.api.facade.FlightFacade;
import technical.test.api.record.FlightRecord;
import technical.test.api.representation.FlightRepresentation;
import technical.test.api.representation.PostFlightRequest;

@RestController
@RequestMapping(FlightEndpoint.BASE_PATH)
@RequiredArgsConstructor
public class FlightEndpoint {

    private static final int DEFAULT_PAGE_SIZE = 6;
    protected static final String BASE_PATH = "/flight";

    private final FlightFacade flightFacade;

    @GetMapping
    public Mono<Page<FlightRepresentation>> getAllFlights(
        @PageableDefault(size = DEFAULT_PAGE_SIZE, sort = "origin", direction = Sort.Direction.DESC) final Pageable pageable) {
        return this.flightFacade.getAllFlights(pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<FlightRecord> saveFlight(@Valid @RequestBody final PostFlightRequest postFlightRequest) {
        return this.flightFacade.saveFlight(postFlightRequest);
    }

}

