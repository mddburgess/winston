package ca.metricalsky.winston.convert.events;

import ca.metricalsky.winston.convert.ConversionServiceAdapter;
import ca.metricalsky.winston.events.model.PullChannelsEvent;
import ca.metricalsky.winston.service.fetch.FetchResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

@Mapper(
        componentModel = "spring",
        uses = ConversionServiceAdapter.class
)
public abstract class FetchResultToPullChannelsEventConverter
        implements Converter<FetchResult, PullChannelsEvent> {

    @Override
    @Mapping(target = "channels", source = "items")
    @Mapping(target = "error", ignore = true)
    public abstract PullChannelsEvent convert(FetchResult source);
}
