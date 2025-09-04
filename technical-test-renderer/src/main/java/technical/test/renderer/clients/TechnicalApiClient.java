package technical.test.renderer.clients;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import technical.test.common.request.PostFlightRequest;
import technical.test.renderer.properties.TechnicalApiProperties;
import technical.test.renderer.viewmodels.FlightViewModel;

@Slf4j
@Component
public class TechnicalApiClient {

    private static final String SLASH = "/";
    private static final String COMMA = ",";
    private static final String PAGE = "page";
    private static final String SIZE = "size";
    private static final String SORT = "sort";
    private final TechnicalApiProperties technicalApiProperties;
    private final WebClient webClient;

    public TechnicalApiClient(TechnicalApiProperties technicalApiProperties, final WebClient.Builder webClientBuilder) {
        this.technicalApiProperties = technicalApiProperties;
        this.webClient = webClientBuilder.build();
    }

    public Mono<PageResponse<FlightViewModel>> getFlights(final Pageable pageable) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
            .fromUriString(this.technicalApiProperties.getUrl() + this.technicalApiProperties.getFlightPath())
            .queryParam(PAGE, pageable.getPageNumber())
            .queryParam(SIZE, pageable.getPageSize());

        pageable.getSort().forEach(order -> {
            uriBuilder.queryParam(SORT, order.getProperty() + COMMA + order.getDirection().name().toLowerCase());
        });

        final String uri = uriBuilder.build().toUriString();

        return this.webClient
            .get()
            .uri(uri)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<>() {});
    }

    public Mono<FlightViewModel> getFlightById(final String id) {
        return this.webClient
            .get()
            .uri(this.technicalApiProperties.getUrl() + this.technicalApiProperties.getFlightPath() + SLASH + id)
            .retrieve()
            .bodyToMono(FlightViewModel.class);
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
