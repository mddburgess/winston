package ca.metricalsky.winston.convert.events;

import ca.metricalsky.winston.api.model.Event;
import ca.metricalsky.winston.api.model.PullOperationEvent;
import ca.metricalsky.winston.convert.ConversionServiceAdapter;
import ca.metricalsky.winston.database.entity.fetch.FetchOperationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

@Mapper(
        componentModel = "spring",
        uses = ConversionServiceAdapter.class
)
public interface FetchOperationEntityToEventConverter
        extends Converter<FetchOperationEntity, Event> {

    @Override
    @Mapping(target = "eventId", ignore = true)
    @Mapping(target = "eventType", constant = "pull-operation")
    @Mapping(target = "operation", source = ".")
    @Mapping(target = "error", ignore = true)
    PullOperationEvent convert(FetchOperationEntity source);
}
