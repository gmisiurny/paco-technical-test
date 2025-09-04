package technical.test.renderer.facades;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import technical.test.common.request.PostFlightRequest;
import technical.test.renderer.services.FlightService;
import technical.test.renderer.viewmodels.FlightViewModel;

@Component
@RequiredArgsConstructor
public class FlightFacade {

    private final FlightService flightService;

    public Mono<Page<FlightViewModel>> getFlights(final Pageable pageable) {
        return this.flightService.getFlights(pageable);
    }

    public Mono<FlightViewModel> getFlightById(final String id) {
        return this.flightService.getFlightById(id);
    }

    public Mono<FlightViewModel> createFlight(final PostFlightRequest createFlightRequest) {
        return this.flightService.createFlight(createFlightRequest);
    }

}
