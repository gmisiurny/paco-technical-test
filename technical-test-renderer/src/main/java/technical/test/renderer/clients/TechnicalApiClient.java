package technical.test.renderer.clients;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import technical.test.common.request.PostFlightRequest;
import technical.test.renderer.properties.TechnicalApiProperties;
import technical.test.renderer.viewmodels.FlightViewModel;

@Component
@Slf4j
public class TechnicalApiClient {

    private final TechnicalApiProperties technicalApiProperties;
    private final WebClient webClient;

    public TechnicalApiClient(TechnicalApiProperties technicalApiProperties, final WebClient.Builder webClientBuilder) {
        this.technicalApiProperties = technicalApiProperties;
        this.webClient = webClientBuilder.build();
    }

    public Mono<PageResponse<FlightViewModel>> getFlights() {
        return this.webClient
                .get()
                .uri(this.technicalApiProperties.getUrl() + this.technicalApiProperties.getFlightPath())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }

    public Mono<FlightViewModel> createFlight(final PostFlightRequest createFlightRequest) {
        return this.webClient
            .post()
            .uri(this.technicalApiProperties.getUrl() + this.technicalApiProperties.getFlightPath())
            .bodyValue(createFlightRequest)
            .retrieve()
            .bodyToMono(FlightViewModel.class);
    }

}
