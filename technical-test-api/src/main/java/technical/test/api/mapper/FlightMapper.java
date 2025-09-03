package technical.test.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import technical.test.api.record.FlightRecord;
import technical.test.api.representation.FlightRepresentation;
import technical.test.common.request.PostFlightRequest;

import java.util.Collections;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, imports = Collections.class)
public interface FlightMapper {

    @Mapping(target = "origin", source = "origin", ignore = true)
    @Mapping(target = "destination", source = "destination", ignore = true)
    FlightRepresentation convert(final FlightRecord source);

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "origin", source = "originId")
    @Mapping(target = "destination", source = "destinationId")
    FlightRecord convert(final PostFlightRequest source);

}
