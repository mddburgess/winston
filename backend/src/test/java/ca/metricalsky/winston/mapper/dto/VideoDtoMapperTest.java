package ca.metricalsky.winston.mapper.dto;

import ca.metricalsky.winston.entity.VideoEntity;
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
                .hasFieldOrPropertyWithValue("channel", null)
                .hasFieldOrPropertyWithValue("title", video.getTitle())
                .hasFieldOrPropertyWithValue("description", video.getDescription())
                .hasFieldOrPropertyWithValue("thumbnailUrl", "/api/v1/videos/" + video.getId() + "/thumbnail")
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
        var videoDto = videoDtoMapper.fromEntity(new VideoEntity());
        assertThat(videoDto)
                .isNotNull()
                .hasNoNullFieldsOrPropertiesExcept("channelId", "channel", "comments");
    }

    private static VideoEntity buildVideo() {
        var video = new VideoEntity();
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
