package ca.metricalsky.winston.service;

import ca.metricalsky.winston.entity.Video;
import ca.metricalsky.winston.entity.view.CommentCount;
import ca.metricalsky.winston.entity.view.VideoCount;
import ca.metricalsky.winston.exception.AppException;
import ca.metricalsky.winston.repository.VideoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static ca.metricalsky.winston.service.ServiceTestConstants.*;
import static org.apache.commons.collections4.map.DefaultedMap.defaultedMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VideoServiceTest {

    private static final String CHANNEL_ID = "channelId";
    private static final String CHANNEL_HANDLE = "@channelHandle";
    private static final List<Video> VIDEOS = VIDEO_IDS.stream().map(VideoServiceTest::buildVideo).toList();
    private static final Map<String, CommentCount> COMMENT_COUNT_MAP = defaultedMap(
            COMMENT_COUNTS.stream().collect(Collectors.toMap(CommentCount::getVideoId, count -> count)),
            new CommentCount.Empty()
    );

    @InjectMocks
    private VideoService videoService;

    @Mock
    private CommentService commentService;
    @Mock
    private VideoRepository videoRepository;
    @Mock
    private VideoCount videoCount;

    @Test
    void countAllByChannelId() {
        when(videoRepository.countAllByChannelId())
                .thenReturn(List.of(videoCount));
        when(videoCount.getChannelId())
                .thenReturn(CHANNEL_ID);
        when(videoCount.getVideos())
                .thenReturn(1L);

        var videoCounts = videoService.countAllByChannelId();

        assertThat(videoCounts)
                .hasSize(1)
                .containsEntry(CHANNEL_ID, 1L);
        assertThat(videoCounts.get("defaultValue"))
                .isEqualTo(0L);
    }

    @Test
    void findAllByChannelHandle() {
        when(videoRepository.findAllByChannelHandle(CHANNEL_HANDLE))
                .thenReturn(VIDEOS);
        when(commentService.getCommentCountsByVideoIds(VIDEO_IDS))
                .thenReturn(COMMENT_COUNT_MAP);

        var videoDtos = videoService.findAllByChannelHandle(CHANNEL_HANDLE);

        assertThat(videoDtos)
                .hasSize(2)
                .allSatisfy(videoDto -> assertThat(videoDto).as(videoDto.getId())
                        .hasFieldOrPropertyWithValue("commentCount",
                                COMMENT_COUNT_MAP.get(videoDto.getId()).getComments())
                        .hasFieldOrPropertyWithValue("replyCount",
                                COMMENT_COUNT_MAP.get(videoDto.getId()).getReplies())
                        .hasFieldOrPropertyWithValue("totalReplyCount",
                                COMMENT_COUNT_MAP.get(videoDto.getId()).getTotalReplies())
                );
    }

    @Test
    void getById() {
        when(videoRepository.findById(VIDEO_ID_WITH_COMMENTS))
                .thenReturn(Optional.of(buildVideo(VIDEO_ID_WITH_COMMENTS)));

        var commentCount = buildCommentCount();
        when(commentService.getCommentCountByVideoId(VIDEO_ID_WITH_COMMENTS))
                .thenReturn(commentCount);

        var videoDto = videoService.getById(VIDEO_ID_WITH_COMMENTS);

        assertThat(videoDto)
                .hasFieldOrPropertyWithValue("id", VIDEO_ID_WITH_COMMENTS)
                .hasFieldOrPropertyWithValue("commentCount", commentCount.getComments())
                .hasFieldOrPropertyWithValue("replyCount", commentCount.getReplies())
                .hasFieldOrPropertyWithValue("totalReplyCount", commentCount.getTotalReplies());
    }

    @Test
    void getById_notFound() {
        when(videoRepository.findById(VIDEO_ID_WITH_COMMENTS))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> videoService.getById(VIDEO_ID_WITH_COMMENTS))
                .isExactlyInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.NOT_FOUND)
                .hasMessageEndingWith("The requested video was not found.");
    }

    @Test
    void getAllById() {
        when(videoRepository.findAllById(VIDEO_IDS))
                .thenReturn(VIDEOS);
        when(commentService.getCommentCountsByVideoIds(VIDEO_IDS))
                .thenReturn(COMMENT_COUNT_MAP);

        var videoDtos = videoService.getAllById(VIDEO_IDS);

        assertThat(videoDtos)
                .hasSize(2)
                .allSatisfy(videoDto -> assertThat(videoDto).as(videoDto.getId())
                        .hasFieldOrPropertyWithValue("commentCount",
                                COMMENT_COUNT_MAP.get(videoDto.getId()).getComments())
                        .hasFieldOrPropertyWithValue("replyCount",
                                COMMENT_COUNT_MAP.get(videoDto.getId()).getReplies())
                        .hasFieldOrPropertyWithValue("totalReplyCount",
                                COMMENT_COUNT_MAP.get(videoDto.getId()).getTotalReplies())
                );
    }

    private static Video buildVideo(String videoId) {
        var video = new Video();
        video.setId(videoId);
        return video;
    }
}
