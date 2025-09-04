package technical.test.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import technical.test.api.record.AirportRecord;
import technical.test.api.record.FlightRecord;
import technical.test.api.representation.FlightRepresentation;
import technical.test.common.request.PostFlightRequest;

import java.util.Collections;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, imports = Collections.class)
public interface FlightMapper {

    @Mapping(target = "id", source = "source.id")
    @Mapping(target = "departure", source = "source.departure")
    @Mapping(target = "arrival", source = "source.arrival")
    @Mapping(target = "price", source = "source.price")
    @Mapping(target = "image", source = "source.image")
    @Mapping(target = "origin", source = "origin")
    @Mapping(target = "destination", source = "destination")
    FlightRepresentation convert(final FlightRecord source, final AirportRecord origin, final AirportRecord destination);

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "origin", source = "originId")
    @Mapping(target = "destination", source = "destinationId")
    FlightRecord convert(final PostFlightRequest source);

}
