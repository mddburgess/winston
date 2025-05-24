package ca.metricalsky.winston.service;

import ca.metricalsky.winston.repository.CommentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static ca.metricalsky.winston.service.ServiceTestConstants.buildCommentCount;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    private static final String VIDEO_ID = "videoId";

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @Test
    void getCommentCountByVideoId() {
        var commentCount = buildCommentCount();
        when(commentRepository.countCommentsForVideoId(VIDEO_ID))
                .thenReturn(commentCount);

        var result = commentService.getCommentCountByVideoId(VIDEO_ID);

        assertThat(result).isEqualTo(commentCount);
    }
}
