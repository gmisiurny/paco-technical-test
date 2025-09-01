package technical.test.api.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import technical.test.api.mapper.AirportMapper;
import technical.test.api.mapper.FlightMapper;
import technical.test.api.record.AirportRecord;
import technical.test.api.record.FlightRecord;
import technical.test.api.representation.FlightRepresentation;
import technical.test.api.representation.PostFlightRequest;
import technical.test.api.services.AirportService;
import technical.test.api.services.FlightService;

@Component
@RequiredArgsConstructor
public class FlightFacade {

    private final FlightService flightService;
    private final AirportService airportService;
    private final FlightMapper flightMapper;
    private final AirportMapper airportMapper;

    public Mono<Page<FlightRepresentation>> getAllFlights(final Pageable pageable) {
        return this.flightService.getAllFlights(pageable)
            .flatMap(page -> {
                final Flux<FlightRepresentation> flightRepresentations = Flux.fromIterable(page.getContent())
                    .flatMap(flightRecord -> this.airportService.findByIataCode(flightRecord.getOrigin())
                        .zipWith(this.airportService.findByIataCode(flightRecord.getDestination()))
                        .flatMap(tuple -> {
                            final AirportRecord origin = tuple.getT1();
                            final AirportRecord destination = tuple.getT2();
                            final FlightRepresentation flightRepresentation = this.flightMapper.convert(flightRecord);
                            flightRepresentation.setOrigin(this.airportMapper.convert(origin));
                            flightRepresentation.setDestination(this.airportMapper.convert(destination));

                            return Mono.just(flightRepresentation);
                        }));

                return flightRepresentations.collectList()
                    .map(content -> new PageImpl<>(content, pageable, page.getTotalElements()));
            });
    }


    public Mono<FlightRecord> saveFlight(final PostFlightRequest postFlightRequest) {
        return this.flightService.saveFlight(this.flightMapper.convert(postFlightRequest));
    }

}
