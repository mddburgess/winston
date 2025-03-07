package ca.metricalsky.yt.comments.mapper.dto;

import ca.metricalsky.yt.comments.dto.ChannelDto;
import ca.metricalsky.yt.comments.entity.Video;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class VideoDtoMapperTest {

    private final VideoDtoMapper videoDtoMapper = new VideoDtoMapperImpl();

    @Test
    void fromEntity() {
        var video = buildVideo();

        var videoDto = videoDtoMapper.fromEntity(video);

        assertThat(videoDto)
                .hasFieldOrPropertyWithValue("id", video.getId())
                .hasFieldOrPropertyWithValue("channelId", video.getChannelId())
                .hasFieldOrPropertyWithValue("channel", new ChannelDto())
                .hasFieldOrPropertyWithValue("title", video.getTitle())
                .hasFieldOrPropertyWithValue("description", video.getDescription())
                .hasFieldOrPropertyWithValue("thumbnailUrl", "/api/videos/" + video.getId() + "/thumbnail")
                .hasFieldOrPropertyWithValue("commentCount", 0L)
                .hasFieldOrPropertyWithValue("replyCount", 0L)
                .hasFieldOrPropertyWithValue("totalReplyCount", 0L)
                .hasFieldOrPropertyWithValue("publishedAt", video.getPublishedAt())
                .hasFieldOrPropertyWithValue("lastFetchedAt", video.getLastFetchedAt());
    }

    @Test
    void fromEntity_nullVideo() {
        var videoDto = videoDtoMapper.fromEntity(null);
        assertThat(videoDto).isNull();
    }

    @Test
    void fromEntity_emptyVideo() {
        var videoDto = videoDtoMapper.fromEntity(new Video());
        assertThat(videoDto)
                .isNotNull()
                .hasNoNullFieldsOrProperties();
    }

    private static Video buildVideo() {
        var video = new Video();
        video.setId("id");
        video.setChannelId("channelId");
        video.setTitle("title");
        video.setDescription("description");
        video.setThumbnailUrl("thumbnailUrl");
        video.setPublishedAt(OffsetDateTime.now());
        video.setLastFetchedAt(OffsetDateTime.now());
        return video;
    }
}
