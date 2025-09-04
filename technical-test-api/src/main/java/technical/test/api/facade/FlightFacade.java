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
import technical.test.api.representation.FlightUserInterfaceFilters;
import technical.test.api.services.AirportService;
import technical.test.api.services.FlightService;
import technical.test.common.request.PostFlightRequest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FlightFacade {

    private final FlightService flightService;
    private final AirportService airportService;
    private final FlightMapper flightMapper;
    private final AirportMapper airportMapper;

    public Mono<Page<FlightRepresentation>> getAllFlights(final Pageable pageable) {
        return this.flightService.getAllFlights(pageable)
            .flatMap(this::enrichFlightPageWithAirports);
    }

    public Mono<Page<FlightRepresentation>> searchFlights(final FlightUserInterfaceFilters filters, final Pageable pageable) {
        return this.flightService.searchFlightsUsingFields(filters, pageable)
            .flatMap(this::enrichFlightPageWithAirports);
    }

    public Mono<FlightRepresentation> saveFlight(final PostFlightRequest postFlightRequest) {
        return this.flightService.saveFlight(this.flightMapper.convert(postFlightRequest))
            .flatMap(this::enrichFlightWithAirports);
    }

    private Mono<Page<FlightRepresentation>> enrichFlightPageWithAirports(Page<FlightRecord> page) {
        final List<FlightRecord> flights = page.getContent();
        final List<Mono<FlightRepresentation>> flightMonos = flights.stream()
            .map(flightRecord -> this.airportService.findByIataCode(flightRecord.getOrigin())
                .zipWith(this.airportService.findByIataCode(flightRecord.getDestination()))
                .map(tuple -> {
                    final AirportRecord origin = tuple.getT1();
                    final AirportRecord destination = tuple.getT2();
                    final FlightRepresentation flightRepresentation = this.flightMapper.convert(flightRecord);
                    flightRepresentation.setOrigin(this.airportMapper.convert(origin));
                    flightRepresentation.setDestination(this.airportMapper.convert(destination));
                    return flightRepresentation;
                }))
            .collect(Collectors.toList());

        return Mono.zip(flightMonos, objects -> Arrays.stream(objects)
                .map(FlightRepresentation.class::cast)
                .collect(Collectors.toList()))
            .map(content -> new PageImpl<>(content, page.getPageable(), page.getTotalElements()));
    }

    private Mono<FlightRepresentation> enrichFlightWithAirports(FlightRecord flightRecord) {
        return this.airportService.findByIataCode(flightRecord.getOrigin())
            .zipWith(this.airportService.findByIataCode(flightRecord.getDestination()))
            .map(tuple -> {
                final AirportRecord origin = tuple.getT1();
                final AirportRecord destination = tuple.getT2();
                final FlightRepresentation flightRepresentation = this.flightMapper.convert(flightRecord);
                flightRepresentation.setOrigin(this.airportMapper.convert(origin));
                flightRepresentation.setDestination(this.airportMapper.convert(destination));

                return flightRepresentation;
            });
    }

}
