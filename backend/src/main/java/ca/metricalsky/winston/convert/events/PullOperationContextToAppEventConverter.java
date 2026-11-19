package ca.metricalsky.winston.convert.events;

import ca.metricalsky.winston.api.model.Event;
import ca.metricalsky.winston.api.model.PullResultsEvent;
import ca.metricalsky.winston.convert.ConversionServiceAdapter;
import ca.metricalsky.winston.domain.PullOperationContext;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

@Mapper(
        componentModel = "spring",
        uses = ConversionServiceAdapter.class
)
public interface PullOperationContextToAppEventConverter
        extends Converter<PullOperationContext, Event> {

    @Override
    @Mapping(target = "eventId", ignore = true)
    @Mapping(target = "eventType", constant = "pull-results")
    @Mapping(target = "results.items", source = "results")
    @Mapping(target = "error", ignore = true)
    PullResultsEvent convert(PullOperationContext source);
}
