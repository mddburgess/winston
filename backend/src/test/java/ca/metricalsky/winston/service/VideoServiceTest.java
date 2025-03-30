package ca.metricalsky.winston.service;

import ca.metricalsky.winston.entity.Video;
import ca.metricalsky.winston.entity.view.CommentCount;
import ca.metricalsky.winston.repository.VideoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ca.metricalsky.winston.service.ServiceTestConstants.*;
import static org.apache.commons.collections4.map.DefaultedMap.defaultedMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VideoServiceTest {

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
