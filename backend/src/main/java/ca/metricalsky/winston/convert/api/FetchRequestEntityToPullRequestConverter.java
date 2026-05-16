package ca.metricalsky.winston.convert.api;

import ca.metricalsky.winston.api.model.PullRequest;
import ca.metricalsky.winston.convert.ConversionServiceAdapter;
import ca.metricalsky.winston.database.entity.fetch.FetchRequestEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

@Mapper(
        componentModel = "spring",
        uses = ConversionServiceAdapter.class
)
public abstract class FetchRequestEntityToPullRequestConverter
        implements Converter<FetchRequestEntity, PullRequest> {

    @Override
    @Mapping(target = "eventSubscriptionId", ignore = true)
    public abstract PullRequest convert(FetchRequestEntity source);
}
