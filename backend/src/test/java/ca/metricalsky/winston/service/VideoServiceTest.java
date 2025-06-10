package ca.metricalsky.winston.service;

import ca.metricalsky.winston.entity.VideoEntity;
import ca.metricalsky.winston.entity.VideoCommentsEntity;
import ca.metricalsky.winston.entity.view.VideoCountView;
import ca.metricalsky.winston.exception.AppException;
import ca.metricalsky.winston.repository.VideoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VideoServiceTest {

    private static final String VIDEO_ID = "videoId";
    private static final String CHANNEL_ID = "channelId";
    private static final String CHANNEL_HANDLE = "@channelHandle";

    @InjectMocks
    private VideoService videoService;

    @Mock
    private VideoRepository videoRepository;
    @Mock
    private VideoCountView videoCount;

    @Test
    void findAllByChannelHandle() {
        when(videoRepository.findAllByChannelHandle(CHANNEL_HANDLE))
                .thenReturn(List.of(buildVideo()));

        var videoDtos = videoService.findAllByChannelHandle(CHANNEL_HANDLE);

        assertThat(videoDtos).first()
                .hasFieldOrPropertyWithValue("id", VIDEO_ID)
                .hasFieldOrPropertyWithValue("comments.commentCount", 1L)
                .hasFieldOrPropertyWithValue("comments.replyCount", 2L)
                .hasFieldOrPropertyWithValue("comments.totalReplyCount", 3L);
    }

    @Test
    void getById() {
        when(videoRepository.findById(VIDEO_ID))
                .thenReturn(Optional.of(buildVideo()));

        var videoDto = videoService.getById(VIDEO_ID);

        assertThat(videoDto)
                .hasFieldOrPropertyWithValue("id", VIDEO_ID)
                .hasFieldOrPropertyWithValue("comments.commentCount", 1L)
                .hasFieldOrPropertyWithValue("comments.replyCount", 2L)
                .hasFieldOrPropertyWithValue("comments.totalReplyCount", 3L);
    }

    @Test
    void getById_notFound() {
        when(videoRepository.findById(VIDEO_ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> videoService.getById(VIDEO_ID))
                .isExactlyInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.NOT_FOUND)
                .hasMessageEndingWith("The requested video was not found.");
    }


    private static VideoEntity buildVideo() {
        var video = new VideoEntity();
        video.setId(VIDEO_ID);
        video.setComments(buildVideoCommentsEntity());
        return video;
    }

    private static VideoCommentsEntity buildVideoCommentsEntity() {
        var videoCommentsEntity = new VideoCommentsEntity();
        videoCommentsEntity.setCommentCount(1);
        videoCommentsEntity.setReplyCount(2);
        videoCommentsEntity.setTotalReplyCount(3);
        return videoCommentsEntity;
    }
}
