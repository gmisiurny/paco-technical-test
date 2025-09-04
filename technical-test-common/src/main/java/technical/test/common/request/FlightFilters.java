package technical.test.common.request;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString
public class FlightFilters {

    private final Set<String> origins;
    private final Set<String> destinations;
    private final Set<Double> prices;

    @Builder
    public FlightFilters(Set<String> origins, Set<String> destinations, Set<Double> prices) {
        this.origins = Objects.requireNonNullElse(origins, new HashSet<>());
        this.destinations = Objects.requireNonNullElse(destinations, new HashSet<>());
        this.prices = Objects.requireNonNullElse(prices, new HashSet<>());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final FlightFilters that = (FlightFilters) o;
        return Objects.equals(this.origins, that.origins)
            && Objects.equals(this.destinations, that.destinations)
            && Objects.equals(this.prices, that.prices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.origins, this.destinations, this.prices);
    }

}
