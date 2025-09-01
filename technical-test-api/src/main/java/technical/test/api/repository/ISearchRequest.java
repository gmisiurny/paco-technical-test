package technical.test.api.repository;


import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.CollectionUtils;
import technical.test.api.representation.Filter;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public interface ISearchRequest {

    List<Filter<?>> filters();

    /**
     * Convertit la recherche en critères MongoDB
     *
     * @return critères de recherche
     */
    default Criteria toCriteria() {
        final Criteria criteria = new Criteria();
        final var filtersMap = this.filters();
        filtersMap.removeIf((filter -> filter.mongoLabel() == null
            || filter.mongoLabel().length == 0
            || filter.dataFilter() == null
            || filter.filterType() == null));

        final List<Criteria> innerCriteria = new ArrayList<>();
        for (final Filter<?> filter : filtersMap) {
            switch (filter.filterType()) {
                case IN -> innerCriteria.addAll(this.handleInFilter(filter));
                case EQUALS -> innerCriteria.addAll(this.handleEqualsFilter(filter));
            }
        }

        return innerCriteria.isEmpty()
            ? criteria
            : criteria.andOperator(innerCriteria);
    }

    private List<Criteria> handleInFilter(final Filter<?> filter) {
        if (!(filter.dataFilter() instanceof final Collection<?> filterBody)) {
            throw new RuntimeException(
                MessageFormat.format("dataFilter ne correspond pas au type requis (critère IN pour {0})", filter.dataFilter()));
        }
        if (CollectionUtils.isEmpty(filterBody)) {
            return Collections.emptyList();
        }
        final Criteria finalCriteria = new Criteria();
        final Criteria[] listCriteria = new Criteria[filter.mongoLabel().length];
        for (int i = 0; i < filter.mongoLabel().length; i++) {
            final Criteria innerCriteria = new Criteria(filter.mongoLabel()[i]);
            innerCriteria.in(filterBody);
            listCriteria[i] = innerCriteria;
        }
        return List.of(finalCriteria.orOperator(listCriteria));
    }

    private List<Criteria> handleEqualsFilter(final Filter<?> filter) {
        if (filter.dataFilter() instanceof final Collection<?> filterBody && CollectionUtils.isEmpty(filterBody)) {
            return Collections.emptyList();
        }
        final Criteria finalCriteria = new Criteria();
        final Criteria[] listCriteria = new Criteria[filter.mongoLabel().length];
        for (int i = 0; i < filter.mongoLabel().length; i++) {
            final Criteria innerCriteria = new Criteria(filter.mongoLabel()[i]);
            innerCriteria.is(filter.dataFilter());
            listCriteria[i] = innerCriteria;
        }
        return List.of(finalCriteria.orOperator(listCriteria));
    }
}


