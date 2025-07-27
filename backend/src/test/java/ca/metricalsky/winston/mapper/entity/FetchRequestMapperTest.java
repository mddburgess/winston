package ca.metricalsky.winston.mapper.entity;

import ca.metricalsky.winston.api.model.FetchRequest;
import ca.metricalsky.winston.entity.fetch.FetchOperationEntity.Type;
import ca.metricalsky.winston.exception.AppException;
import ca.metricalsky.winston.repository.VideoRepository;
import ca.metricalsky.winston.service.ChannelService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FetchRequestMapperTest {

    @InjectMocks
    private FetchRequestMapper mapper;

    @Mock
    private ChannelService channelService;
    @Mock
    private VideoRepository videoRepository;

    @Test
    void toFetchRequestEntity_fetchChannel() {
        var fetchRequest = new FetchRequest()
                .fetch(FetchRequest.FetchEnum.CHANNEL)
                .channelHandle("@handle");

        var fetchRequestEntity = mapper.toFetchRequestEntity(fetchRequest);

        assertThat(fetchRequestEntity.getOperations())
                .hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue("operationType", Type.CHANNELS)
                .hasFieldOrPropertyWithValue("objectId", fetchRequest.getChannelHandle());
    }

    @Test
    void toFetchRequestEntity_fetchAllVideos() {
        var fetchRequest = new FetchRequest()
                .fetch(FetchRequest.FetchEnum.VIDEOS)
                .channelId("channelId")
                .range(FetchRequest.RangeEnum.ALL);

        var fetchRequestEntity = mapper.toFetchRequestEntity(fetchRequest);

        assertThat(fetchRequestEntity.getOperations())
                .hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue("operationType", Type.VIDEOS)
                .hasFieldOrPropertyWithValue("objectId", fetchRequest.getChannelId())
                .hasFieldOrPropertyWithValue("mode", "ALL");
    }

    @Test
    void toFetchRequestEntity_fetchLatestVideos() {
        var fetchRequest = new FetchRequest()
                .fetch(FetchRequest.FetchEnum.VIDEOS)
                .channelId("channelId")
                .range(FetchRequest.RangeEnum.LATEST);

        when(videoRepository.findLastPublishedAtForChannelId(fetchRequest.getChannelId()))
                .thenReturn(Optional.of(OffsetDateTime.parse("2025-01-01T00:00:00Z")));

        var fetchRequestEntity = mapper.toFetchRequestEntity(fetchRequest);

        assertThat(fetchRequestEntity.getOperations())
                .hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue("operationType", Type.VIDEOS)
                .hasFieldOrPropertyWithValue("objectId", fetchRequest.getChannelId())
                .hasFieldOrPropertyWithValue("mode", "LATEST")
                .hasFieldOrPropertyWithValue("publishedAfter", OffsetDateTime.parse("2025-01-01T00:00:01Z"))
                .hasFieldOrPropertyWithValue("publishedBefore", null);
    }

    @Test
    void toFetchRequestEntity_fetchComments() {
        var fetchRequest = new FetchRequest()
                .fetch(FetchRequest.FetchEnum.COMMENTS)
                .videoId("videoId");

        var fetchRequestEntity = mapper.toFetchRequestEntity(fetchRequest);

        assertThat(fetchRequestEntity.getOperations())
                .hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue("operationType", Type.COMMENTS)
                .hasFieldOrPropertyWithValue("objectId", fetchRequest.getVideoId());
    }

    @Test
    void toFetchRequestEntity_fetchRepliesForComment() {
        var fetchRequest = new FetchRequest()
                .fetch(FetchRequest.FetchEnum.REPLIES)
                .commentId("commentId");

        var fetchRequestEntity = mapper.toFetchRequestEntity(fetchRequest);

        assertThat(fetchRequestEntity.getOperations())
                .hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue("operationType", Type.REPLIES)
                .hasFieldOrPropertyWithValue("objectId", fetchRequest.getCommentId())
                .hasFieldOrPropertyWithValue("mode", "FOR_COMMENT");
    }

    @Test
    void toFetchRequestEntity_fetchRepliesForVideo() {
        var fetchRequest = new FetchRequest()
                .fetch(FetchRequest.FetchEnum.REPLIES)
                .videoId("videoId");

        var fetchRequestEntity = mapper.toFetchRequestEntity(fetchRequest);

        assertThat(fetchRequestEntity.getOperations())
                .hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue("operationType", Type.REPLIES)
                .hasFieldOrPropertyWithValue("objectId", fetchRequest.getVideoId())
                .hasFieldOrPropertyWithValue("mode", "FOR_VIDEO");
    }

    @Test
    void toFetchRequestEntity_fetchReplies_forCommentHasPriority() {
        var fetchRequest = new FetchRequest()
                .fetch(FetchRequest.FetchEnum.REPLIES)
                .commentId("commentId")
                .videoId("videoId");

        var fetchRequestEntity = mapper.toFetchRequestEntity(fetchRequest);

        assertThat(fetchRequestEntity.getOperations())
                .hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue("operationType", Type.REPLIES)
                .hasFieldOrPropertyWithValue("objectId", fetchRequest.getCommentId())
                .hasFieldOrPropertyWithValue("mode", "FOR_COMMENT");
    }

    @Test
    void toFetchRequestEntity_badRequest() {
        var fetchRequest = new FetchRequest();

        assertThatThrownBy(() -> mapper.toFetchRequestEntity(fetchRequest))
                .isExactlyInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST)
                .hasMessageEndingWith("The request is syntactically invalid and cannot be processed.");
    }
}
