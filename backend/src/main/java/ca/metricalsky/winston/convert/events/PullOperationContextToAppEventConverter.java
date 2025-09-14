package ca.metricalsky.winston.convert.events;

import ca.metricalsky.winston.convert.ConversionServiceAdapter;
import ca.metricalsky.winston.domain.PullOperationContext;
import ca.metricalsky.winston.events.model.AppEvent;
import ca.metricalsky.winston.events.model.PullResultsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

@Mapper(
        componentModel = "spring",
        uses = ConversionServiceAdapter.class
)
public interface PullOperationContextToAppEventConverter
        extends Converter<PullOperationContext, AppEvent> {

    @Override
    @Mapping(target = "items", source = "results")
    @Mapping(target = "error", ignore = true)
    PullResultsEvent<Object> convert(PullOperationContext source);
}
