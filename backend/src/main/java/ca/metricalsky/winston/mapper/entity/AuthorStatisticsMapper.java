package ca.metricalsky.winston.mapper.entity;

import ca.metricalsky.winston.api.model.AuthorStatistics;
import ca.metricalsky.winston.api.model.VideoStatistics;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthorStatisticsMapper implements Converter<List<VideoStatistics>, AuthorStatistics> {

    @Override
    public AuthorStatistics convert(List<VideoStatistics> source) {
        var channelCount = source.stream().map(VideoStatistics::getChannelId).distinct().count();
        var videoCount = source.stream().map(VideoStatistics::getVideoId).distinct().count();
        var commentCount = source.stream().mapToLong(VideoStatistics::getCommentCount).sum();
        var replyCount = source.stream().mapToLong(VideoStatistics::getReplyCount).sum();

        return new AuthorStatistics()
                .channelCount(channelCount)
                .videoCount(videoCount)
                .commentCount(commentCount)
                .replyCount(replyCount);
    }
}
