package ca.metricalsky.winston.convert.events;

import ca.metricalsky.winston.api.model.Event;
import ca.metricalsky.winston.api.model.PullResults;
import ca.metricalsky.winston.api.model.PullResultsEvent;
import ca.metricalsky.winston.convert.ConversionServiceAdapter;
import ca.metricalsky.winston.domain.PullOperationContext;
import org.apache.commons.collections4.ListUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

@Mapper(
        componentModel = "spring",
        uses = ConversionServiceAdapter.class
)
public abstract class PullOperationContextToAppEventConverter
        implements Converter<PullOperationContext, Event> {

    @Override
    @Mapping(target = "eventId", ignore = true)
    @Mapping(target = "eventType", constant = "pull-results")
    @Mapping(target = "results", source = ".")
    @Mapping(target = "error", ignore = true)
    public abstract PullResultsEvent convert(PullOperationContext source);

    protected PullResults mapResults(PullOperationContext source) {
        return new PullResults()
                .count(ListUtils.emptyIfNull(source.getResults()).size())
                .totalCount(source.getTotalCount())
                .items(source.getResults());
    }
}
