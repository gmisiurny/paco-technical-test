package technical.test.api.representation;

import lombok.EqualsAndHashCode;
import technical.test.api.repository.ISearchRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
public class FlightUserInterfaceFilters extends FlightFilters implements ISearchRequest {

    public FlightUserInterfaceFilters(Set<String> origins, Set<String> destinations, Set<Double> prices) {
        super(origins, destinations, prices);
    }

    @Override
    public List<Filter<?>> filters() {
        final List<Filter<?>> filters = new ArrayList<>();

        filters.add(new Filter<>(this.getOrigins(), FilterType.IN, "origin"));
        filters.add(new Filter<>(this.getDestinations(), FilterType.IN, "destination"));
        filters.add(new Filter<>(this.getPrices(), FilterType.IN, "price"));

        return filters;
    }

}
