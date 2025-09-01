package technical.test.api.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import technical.test.api.record.FlightRecord;
import technical.test.api.repository.FlightRepository;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;

    public Mono<Page<FlightRecord>> getAllFlights(final Pageable pageable) {
        return this.flightRepository.findAllBy()
            .skip(pageable.getOffset())
            .take(pageable.getPageSize())
            .collectList()
            .zipWith(this.flightRepository.count())
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
