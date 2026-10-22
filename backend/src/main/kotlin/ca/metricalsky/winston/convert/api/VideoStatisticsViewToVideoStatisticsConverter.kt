package ca.metricalsky.winston.convert.api

import ca.metricalsky.winston.api.model.VideoStatistics
import ca.metricalsky.winston.database.view.VideoStatisticsView
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class VideoStatisticsViewToVideoStatisticsConverter: Converter<VideoStatisticsView, VideoStatistics> {

    override fun convert(source: VideoStatisticsView) = VideoStatistics()
        .channelId(source.getChannelId())
        .videoId(source.getVideoId())
        .commentCount(source.getCommentCount())
        .replyCount(source.getReplyCount())
        .lastCommentedAt(source.getLastCommentedAt())
}
