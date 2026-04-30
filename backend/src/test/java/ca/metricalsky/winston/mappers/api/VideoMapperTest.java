package ca.metricalsky.winston.mappers.api;

import ca.metricalsky.winston.database.entity.video.VideoCommentsEntity;
import ca.metricalsky.winston.database.entity.channel.ChannelEntity;
import ca.metricalsky.winston.entity.VideoEntity;
import ca.metricalsky.winston.entity.view.ChannelVideoView;
import ca.metricalsky.winston.test.TestUtils;
import ca.metricalsky.winston.test.faker.WinstonFaker;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

class VideoMapperTest {

    private static final WinstonFaker faker = new WinstonFaker();

    private final VideoMapper videoMapper = new VideoMapperImpl();

    @Test
    void toVideo_videoEntity() {
        var videoEntity = buildVideoEntity();
        var videoCommentsEntity = videoEntity.getComments();

        var video = videoMapper.toVideo(videoEntity);

        assertThat(video)
                .as("video")
                .hasFieldOrPropertyWithValue("id", videoEntity.getId())
                .hasFieldOrPropertyWithValue("title", videoEntity.getTitle())
                .hasFieldOrPropertyWithValue("description", videoEntity.getDescription())
                .hasFieldOrPropertyWithValue("thumbnailUrl",
                        "/api/v1/videos/" + videoEntity.getId() + "/thumbnail")
                .hasFieldOrPropertyWithValue("publishedAt", video.getPublishedAt())
                .hasFieldOrPropertyWithValue("lastFetchedAt", video.getLastFetchedAt())
                .hasNoNullFieldsOrPropertiesExcept("channel", "details");
        assertThat(video.getComments())
                .as("video comments")
                .hasFieldOrPropertyWithValue("commentsDisabled", videoCommentsEntity.getCommentsDisabled())
                .hasFieldOrPropertyWithValue("commentCount", (int) videoCommentsEntity.getCommentCount())
                .hasFieldOrPropertyWithValue("replyCount", (int) videoCommentsEntity.getReplyCount())
                .hasFieldOrPropertyWithValue("totalReplyCount", (int) videoCommentsEntity.getTotalReplyCount())
                .hasFieldOrPropertyWithValue("lastFetchedAt", videoEntity.getComments().getLastFetchedAt())
                .hasNoNullFieldsOrProperties();
    }

    @Test
    void toVideo_nullVideoEntity() {
        var video = videoMapper.toVideo((VideoEntity) null);

        assertThat(video)
                .isNull();
    }

    @Test
    void toVideo_emptyVideoEntity() {
        var video = videoMapper.toVideo(new VideoEntity());

        assertThat(video)
                .hasAllNullFieldsOrProperties();
    }

    @Test
    void toVideo_channelVideoView() {
        var channelVideoView = mockChannelVideoView();
        var channelEntity = channelVideoView.getChannel();
        var videoEntity = channelVideoView.getVideo();
        var videoCommentsEntity = videoEntity.getComments();

        var video = videoMapper.toVideo(channelVideoView);

        assertThat(video)
                .as("video")
                .hasFieldOrPropertyWithValue("id", videoEntity.getId())
                .hasFieldOrPropertyWithValue("title", videoEntity.getTitle())
                .hasFieldOrPropertyWithValue("description", videoEntity.getDescription())
                .hasFieldOrPropertyWithValue("channel.title", channelEntity.getTitle())
                .hasFieldOrPropertyWithValue("channel.handle", channelEntity.getCustomUrl())
                .hasFieldOrPropertyWithValue("thumbnailUrl",
                        "/api/v1/videos/" + videoEntity.getId() + "/thumbnail")
                .hasFieldOrPropertyWithValue("publishedAt", video.getPublishedAt())
                .hasFieldOrPropertyWithValue("lastFetchedAt", video.getLastFetchedAt())
                .hasNoNullFieldsOrPropertiesExcept("details");
        assertThat(video.getComments())
                .as("video comments")
                .hasFieldOrPropertyWithValue("commentsDisabled", videoCommentsEntity.getCommentsDisabled())
                .hasFieldOrPropertyWithValue("commentCount", (int) videoCommentsEntity.getCommentCount())
                .hasFieldOrPropertyWithValue("replyCount", (int) videoCommentsEntity.getReplyCount())
                .hasFieldOrPropertyWithValue("totalReplyCount", (int) videoCommentsEntity.getTotalReplyCount())
                .hasFieldOrPropertyWithValue("lastFetchedAt", videoEntity.getComments().getLastFetchedAt())
                .hasNoNullFieldsOrProperties();
    }

    @Test
    void toVideo_nullChannelVideoView() {
        var video = videoMapper.toVideo((ChannelVideoView) null);

        assertThat(video)
                .isNull();
    }

    private static ChannelVideoView mockChannelVideoView() {
        var channelVideoView = mock(ChannelVideoView.class);
        lenient().when(channelVideoView.getChannel())
                .thenReturn(buildChannelEntity());
        lenient().when(channelVideoView.getVideo())
                .thenReturn(buildVideoEntity());
        return channelVideoView;
    }

    private static ChannelEntity buildChannelEntity() {
        var channelEntity = new ChannelEntity();
        channelEntity.setTitle(TestUtils.randomString());
        channelEntity.setCustomUrl(TestUtils.randomString());
        return channelEntity;
    }

    private static VideoEntity buildVideoEntity() {
        var videoId = faker.youtube().videoId();
        var videoCommentsEntity = new VideoCommentsEntity(videoId, false, 1, 2, 3, OffsetDateTime.now());
        return VideoEntity.builder()
                .id(videoId)
                .channelId(TestUtils.randomId())
                .title(TestUtils.randomString())
                .description(TestUtils.randomString())
                .thumbnailUrl(TestUtils.randomString())
                .publishedAt(OffsetDateTime.now())
                .lastFetchedAt(OffsetDateTime.now())
                .comments(videoCommentsEntity)
                .build();
    }
}
