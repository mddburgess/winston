package ca.metricalsky.winston.mappers.api;

import ca.metricalsky.winston.api.model.Video;
import ca.metricalsky.winston.entity.VideoEntity;
import ca.metricalsky.winston.entity.view.ChannelVideoView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class VideoMapper {

    @Mapping(target = "thumbnailUrl", source = ".")
    @Mapping(target = "channel", ignore = true)
    public abstract Video toVideo(VideoEntity videoEntity);

    @Mapping(target = "id", source = "video.id")
    @Mapping(target = "title", source = "video.title")
    @Mapping(target = "description", source = "video.description")
    @Mapping(target = "channel.handle", source = "channel.customUrl")
    @Mapping(target = "thumbnailUrl", source = "video")
    @Mapping(target = "comments", source = "video.comments")
    @Mapping(target = "publishedAt", source = "video.publishedAt")
    @Mapping(target = "lastFetchedAt", source = "video.lastFetchedAt")
    public abstract Video toVideo(ChannelVideoView channelVideoView);

    protected String getThumbnailUrl(VideoEntity videoEntity) {
        return videoEntity.getThumbnailUrl() != null
                ? "/api/v1/videos/" + videoEntity.getId() + "/thumbnail"
                : "";
    }
}
