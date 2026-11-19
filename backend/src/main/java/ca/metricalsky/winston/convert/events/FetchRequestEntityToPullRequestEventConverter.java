package ca.metricalsky.winston.convert.events;

import ca.metricalsky.winston.api.model.Event;
import ca.metricalsky.winston.api.model.PullRequestEvent;
import ca.metricalsky.winston.convert.ConversionServiceAdapter;
import ca.metricalsky.winston.database.entity.fetch.FetchRequestEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

@Mapper(
        componentModel = "spring",
        uses = ConversionServiceAdapter.class
)
public interface FetchRequestEntityToPullRequestEventConverter
        extends Converter<FetchRequestEntity, Event> {

    @Override
    @Mapping(target = "eventId", ignore = true)
    @Mapping(target = "eventType", constant = "pull-request")
    @Mapping(target = "request", source = ".")
    @Mapping(target = "error", ignore = true)
    PullRequestEvent convert(FetchRequestEntity source);
}
