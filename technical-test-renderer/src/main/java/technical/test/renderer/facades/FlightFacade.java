package technical.test.renderer.facades;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import technical.test.common.request.PostFlightRequest;
import technical.test.renderer.services.FlightService;
import technical.test.renderer.viewmodels.FlightViewModel;

@Component
@RequiredArgsConstructor
public class FlightFacade {

    private final FlightService flightService;

    public Mono<Page<FlightViewModel>> getFlights() {
        return this.flightService.getFlights();
    }

    public Mono<FlightViewModel> createFlight(final PostFlightRequest createFlightRequest) {
        return this.flightService.createFlight(createFlightRequest);
    }

}
