package ca.metricalsky.winston.mapper.entity;

import ca.metricalsky.winston.dto.fetch.FetchChannel;
import ca.metricalsky.winston.dto.fetch.FetchComments;
import ca.metricalsky.winston.dto.fetch.FetchReplies;
import ca.metricalsky.winston.dto.fetch.FetchRequestDto;
import ca.metricalsky.winston.dto.fetch.FetchVideos;
import ca.metricalsky.winston.entity.fetch.FetchRequestEntity;
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
    void toFetchRequest_fetchChannel() {
        var fetchChannel = new FetchChannel();
        fetchChannel.setHandle("@handle");

        var fetchRequestDto = new FetchRequestDto();
        fetchRequestDto.setChannel(fetchChannel);

        var fetchRequest = mapper.toFetchRequest(fetchRequestDto);

        assertThat(fetchRequest)
                .hasFieldOrPropertyWithValue("fetchType", FetchRequestEntity.FetchType.CHANNELS)
                .hasFieldOrPropertyWithValue("objectId", fetchChannel.getHandle());
    }

    @Test
    void toFetchRequest_fetchAllVideos() {
        var fetchVideos = new FetchVideos();
        fetchVideos.setChannelId("channelId");
        fetchVideos.setFetch(FetchVideos.Mode.ALL);

        var fetchRequestDto = new FetchRequestDto();
        fetchRequestDto.setVideos(fetchVideos);

        var fetchRequest = mapper.toFetchRequest(fetchRequestDto);

        assertThat(fetchRequest)
                .hasFieldOrPropertyWithValue("fetchType", FetchRequestEntity.FetchType.VIDEOS)
                .hasFieldOrPropertyWithValue("objectId", fetchVideos.getChannelId())
                .hasFieldOrPropertyWithValue("mode", "ALL");
    }

    @Test
    void toFetchRequest_fetchLatestVideos() {
        var fetchVideos = new FetchVideos();
        fetchVideos.setChannelId("channelId");
        fetchVideos.setFetch(FetchVideos.Mode.LATEST);

        var fetchRequestDto = new FetchRequestDto();
        fetchRequestDto.setVideos(fetchVideos);

        when(videoRepository.findLastPublishedAtForChannelId(fetchVideos.getChannelId()))
                .thenReturn(Optional.of(OffsetDateTime.parse("2025-01-01T00:00:00Z")));

        var fetchRequest = mapper.toFetchRequest(fetchRequestDto);

        assertThat(fetchRequest)
                .hasFieldOrPropertyWithValue("fetchType", FetchRequestEntity.FetchType.VIDEOS)
                .hasFieldOrPropertyWithValue("objectId", fetchVideos.getChannelId())
                .hasFieldOrPropertyWithValue("mode", "LATEST")
                .hasFieldOrPropertyWithValue("publishedAfter", OffsetDateTime.parse("2025-01-01T00:00:01Z"))
                .hasFieldOrPropertyWithValue("publishedBefore", null);
    }

    @Test
    void toFetchRequest_fetchComments() {
        var fetchComments = new FetchComments();
        fetchComments.setVideoId("videoId");

        var fetchRequestDto = new FetchRequestDto();
        fetchRequestDto.setComments(fetchComments);

        var fetchRequest = mapper.toFetchRequest(fetchRequestDto);

        assertThat(fetchRequest)
                .hasFieldOrPropertyWithValue("fetchType", FetchRequestEntity.FetchType.COMMENTS)
                .hasFieldOrPropertyWithValue("objectId", fetchComments.getVideoId());
    }

    @Test
    void toFetchRequest_fetchRepliesForComment() {
        var fetchReplies = new FetchReplies();
        fetchReplies.setCommentId("commentId");

        var fetchRequestDto = new FetchRequestDto();
        fetchRequestDto.setReplies(fetchReplies);

        var fetchRequest = mapper.toFetchRequest(fetchRequestDto);

        assertThat(fetchRequest)
                .hasFieldOrPropertyWithValue("fetchType", FetchRequestEntity.FetchType.REPLIES)
                .hasFieldOrPropertyWithValue("objectId", fetchReplies.getCommentId())
                .hasFieldOrPropertyWithValue("mode", "FOR_COMMENT");
    }

    @Test
    void toFetchRequest_fetchRepliesForVideo() {
        var fetchReplies = new FetchReplies();
        fetchReplies.setVideoId("videoId");

        var fetchRequestDto = new FetchRequestDto();
        fetchRequestDto.setReplies(fetchReplies);

        var fetchRequest = mapper.toFetchRequest(fetchRequestDto);

        assertThat(fetchRequest)
                .hasFieldOrPropertyWithValue("fetchType", FetchRequestEntity.FetchType.REPLIES)
                .hasFieldOrPropertyWithValue("objectId", fetchReplies.getVideoId())
                .hasFieldOrPropertyWithValue("mode", "FOR_VIDEO");
    }

    @Test
    void toFetchRequest_fetchReplies_forCommentHasPriority() {
        var fetchReplies = new FetchReplies();
        fetchReplies.setVideoId("videoId");
        fetchReplies.setCommentId("commentId");

        var fetchRequestDto = new FetchRequestDto();
        fetchRequestDto.setReplies(fetchReplies);

        var fetchRequest = mapper.toFetchRequest(fetchRequestDto);

        assertThat(fetchRequest)
                .hasFieldOrPropertyWithValue("fetchType", FetchRequestEntity.FetchType.REPLIES)
                .hasFieldOrPropertyWithValue("objectId", fetchReplies.getCommentId())
                .hasFieldOrPropertyWithValue("mode", "FOR_COMMENT");
    }

    @Test
    void toFetchRequest_badRequest() {
        var fetchRequestDto = new FetchRequestDto();

        assertThatThrownBy(() -> mapper.toFetchRequest(fetchRequestDto))
                .isExactlyInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST)
                .hasMessageEndingWith("The request is syntactically invalid and cannot be processed.");
    }
}
