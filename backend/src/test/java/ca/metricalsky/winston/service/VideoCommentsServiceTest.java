package ca.metricalsky.winston.service;

import ca.metricalsky.winston.entity.VideoCommentsEntity;
import ca.metricalsky.winston.entity.view.CommentCount;
import ca.metricalsky.winston.repository.VideoCommentsRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VideoCommentsServiceTest {

    private static final String VIDEO_ID = "videoId";

    @InjectMocks
    private VideoCommentsService videoCommentsService;

    @Mock
    private CommentCount commentCount;
    @Mock
    private CommentService commentService;
    @Mock
    private VideoCommentsRepository videoCommentsRepository;
    @Captor
    private ArgumentCaptor<VideoCommentsEntity> videoCommentsEntity;

    @ParameterizedTest(name = "{0}")
    @MethodSource("videoCommentsSource")
    void updateVideoComments(String testCase, VideoCommentsEntity existingEntity) {
        when(videoCommentsRepository.findById(VIDEO_ID))
                .thenReturn(Optional.ofNullable(existingEntity));
        when(commentService.getCommentCountByVideoId(VIDEO_ID))
                .thenReturn(commentCount);
        when(commentCount.getComments()).thenReturn(2L);
        when(commentCount.getReplies()).thenReturn(3L);
        when(commentCount.getTotalReplies()).thenReturn(4L);

        videoCommentsService.updateVideoComments(VIDEO_ID);

        verify(videoCommentsRepository).save(videoCommentsEntity.capture());

        assertThat(videoCommentsEntity.getValue())
                .hasFieldOrPropertyWithValue("videoId", VIDEO_ID)
                .hasFieldOrPropertyWithValue("commentCount", commentCount.getComments())
                .hasFieldOrPropertyWithValue("replyCount", commentCount.getReplies())
                .hasFieldOrPropertyWithValue("totalReplyCount", commentCount.getTotalReplies());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("videoCommentsSource")
    void markVideoCommentsDisabled(String testCase, VideoCommentsEntity existingEntity) {
        when(videoCommentsRepository.findById(VIDEO_ID))
                .thenReturn(Optional.ofNullable(existingEntity));

        videoCommentsService.markVideoCommentsDisabled(VIDEO_ID);

        verify(videoCommentsRepository).save(videoCommentsEntity.capture());

        assertThat(videoCommentsEntity.getValue())
                .hasFieldOrPropertyWithValue("videoId", VIDEO_ID)
                .hasFieldOrPropertyWithValue("commentsDisabled", true);
    }

    private static List<Arguments> videoCommentsSource() {
        return List.of(
                arguments("insert new record", null),
                arguments("update existing record", buildVideoCommentsEntity())
        );
    }

    private static VideoCommentsEntity buildVideoCommentsEntity() {
        var videoCommentsEntity = new VideoCommentsEntity();
        videoCommentsEntity.setVideoId(VIDEO_ID);
        videoCommentsEntity.setCommentsDisabled(false);
        videoCommentsEntity.setCommentCount(1);
        videoCommentsEntity.setReplyCount(1);
        videoCommentsEntity.setTotalReplyCount(1);
        return videoCommentsEntity;
    }
}
