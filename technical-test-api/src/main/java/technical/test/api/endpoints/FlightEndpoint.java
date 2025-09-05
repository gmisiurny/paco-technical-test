package technical.test.api.endpoints;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import technical.test.api.facade.FlightFacade;
import technical.test.api.representation.FlightRepresentation;
import technical.test.api.representation.FlightUserInterfaceFilters;
import technical.test.common.request.PostFlightRequest;

@RestController
@RequestMapping(FlightEndpoint.BASE_PATH)
@RequiredArgsConstructor
public class FlightEndpoint {

    protected static final String BASE_PATH = "/flight";
    private static final String SEARCH_PATH = "/search";
    private static final int DEFAULT_PAGE_SIZE = 2000;

    private final FlightFacade flightFacade;


    @GetMapping
    @ResponseStatus(HttpStatus.PARTIAL_CONTENT)
    public Mono<Page<FlightRepresentation>> getAllFlights(
        @PageableDefault(size = DEFAULT_PAGE_SIZE, sort = "price", direction = Sort.Direction.ASC) final Pageable pageable) {
        return this.flightFacade.getAllFlights(pageable);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<FlightRepresentation> getFlightById(@PathVariable final String id) {
        return this.flightFacade.getFlightById(id);
    }

    @PostMapping(SEARCH_PATH)
    @ResponseStatus(HttpStatus.PARTIAL_CONTENT)
    public Mono<Page<FlightRepresentation>> searchFlights(
        @RequestBody(required = false) final FlightUserInterfaceFilters filters,
        @PageableDefault(size = DEFAULT_PAGE_SIZE, sort = "price", direction = Sort.Direction.ASC) final Pageable pageable) {
        return this.flightFacade.searchFlights(filters, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<FlightRepresentation> saveFlight(@Valid @RequestBody final PostFlightRequest postFlightRequest) {
        return this.flightFacade.saveFlight(postFlightRequest);
    }

}

