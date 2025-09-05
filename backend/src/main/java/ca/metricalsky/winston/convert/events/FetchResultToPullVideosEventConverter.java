package ca.metricalsky.winston.convert.events;

import ca.metricalsky.winston.convert.ConversionServiceAdapter;
import ca.metricalsky.winston.events.model.PullVideosEvent;
import ca.metricalsky.winston.service.fetch.FetchResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

@Mapper(
        componentModel = "spring",
        uses = ConversionServiceAdapter.class
)
public abstract class FetchResultToPullVideosEventConverter
        implements Converter<FetchResult, PullVideosEvent> {

    @Override
    @Mapping(target = "channelHandle", source = "objectId")
    @Mapping(target = "videos", source = "items")
    @Mapping(target = "error", ignore = true)
    public abstract PullVideosEvent convert(FetchResult source);
}
