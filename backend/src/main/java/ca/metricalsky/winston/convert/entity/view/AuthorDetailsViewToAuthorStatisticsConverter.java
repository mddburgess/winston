package ca.metricalsky.winston.convert.entity.view;

import ca.metricalsky.winston.api.model.AuthorStatistics;
import ca.metricalsky.winston.entity.view.AuthorDetailsView;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring")
public interface AuthorDetailsViewToAuthorStatisticsConverter
        extends Converter<AuthorDetailsView, AuthorStatistics> {

    @Override
    AuthorStatistics convert(AuthorDetailsView source);
}
