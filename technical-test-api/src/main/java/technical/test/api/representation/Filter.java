package technical.test.api.representation;


public record Filter<T>(
    T dataFilter,
    FilterType filterType,
    String... mongoLabel
) {
}

