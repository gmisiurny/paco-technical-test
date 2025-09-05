package technical.test.api.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import technical.test.api.record.FlightRecord;
import technical.test.api.repository.FlightRepository;
import technical.test.api.repository.ISearchRequest;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;
    private final ReactiveMongoTemplate mongoTemplate;


    public Mono<Page<FlightRecord>> getAllFlights(final Pageable pageable) {
        final Query query = new Query();
        query.with(pageable);

        return this.mongoTemplate.find(query, FlightRecord.class)
            .collectList()
            .zipWith(this.mongoTemplate.count(new Query(), FlightRecord.class))
            .map(tuple -> new PageImpl<>(
                tuple.getT1(),
                pageable,
                tuple.getT2()
            ));
    }

    public Mono<FlightRecord> getFlightById(final String id) {
        return this.flightRepository.findById(UUID.fromString(id));
    }

    public Mono<Page<FlightRecord>> searchFlightsUsingFields(final ISearchRequest searchRequest, final Pageable pageable) {
        if (Objects.isNull(searchRequest)) {
            return this.getAllFlights(pageable);
        }

        final Query query = new Query(searchRequest.toCriteria());
        query.with(pageable);

        return this.mongoTemplate.find(query, FlightRecord.class)
            .collectList()
            .zipWith(this.mongoTemplate.count(new Query(searchRequest.toCriteria()), FlightRecord.class))
            .map(tuple -> new PageImpl<>(
                tuple.getT1(),
                pageable,
                tuple.getT2()
            ));
    }

    public Mono<FlightRecord> saveFlight(final FlightRecord flightRecord) {
        return this.flightRepository.save(flightRecord);
    }

}
