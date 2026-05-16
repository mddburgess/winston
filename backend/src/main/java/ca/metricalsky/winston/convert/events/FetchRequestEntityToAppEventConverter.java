package ca.metricalsky.winston.convert.events;

import ca.metricalsky.winston.convert.ConversionServiceAdapter;
import ca.metricalsky.winston.database.entity.fetch.FetchRequestEntity;
import ca.metricalsky.winston.events.model.AppEvent;
import ca.metricalsky.winston.events.model.PullRequestEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

@Mapper(
        componentModel = "spring",
        uses = ConversionServiceAdapter.class
)
public interface FetchRequestEntityToAppEventConverter
        extends Converter<FetchRequestEntity, AppEvent> {

    @Override
    @Mapping(target = "request", source = ".")
    @Mapping(target = "error", ignore = true)
    PullRequestEvent convert(FetchRequestEntity source);
}
