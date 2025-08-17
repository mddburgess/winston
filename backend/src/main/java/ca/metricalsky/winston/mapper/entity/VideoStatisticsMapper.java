package ca.metricalsky.winston.mapper.entity;

import ca.metricalsky.winston.api.model.VideoStatistics;
import ca.metricalsky.winston.entity.view.VideoStatisticsView;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring")
public interface VideoStatisticsMapper extends Converter<VideoStatisticsView, VideoStatistics> {

}
