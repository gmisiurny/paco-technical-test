package technical.test.renderer.services;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import technical.test.common.request.PostFlightRequest;
import technical.test.renderer.clients.PageResponse;
import technical.test.renderer.clients.TechnicalApiClient;
import technical.test.renderer.viewmodels.FlightViewModel;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final TechnicalApiClient technicalApiClient;

    public Mono<Page<FlightViewModel>> getFlights() {
        return this.technicalApiClient.getFlights()
            .map(PageResponse::toPage);
    }

    public Mono<FlightViewModel> createFlight(final PostFlightRequest createFlightRequest) {
        return this.technicalApiClient.createFlight(createFlightRequest);
    }

}
