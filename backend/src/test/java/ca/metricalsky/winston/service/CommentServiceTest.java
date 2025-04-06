package ca.metricalsky.winston.service;

import ca.metricalsky.winston.repository.CommentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static ca.metricalsky.winston.service.ServiceTestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @Test
    void getCommentCountsByVideoIds() {
        when(commentRepository.countCommentsForVideoIds(VIDEO_IDS)).thenReturn(COMMENT_COUNTS);

        var commentCounts = commentService.getCommentCountsByVideoIds(VIDEO_IDS);

        assertThat(commentCounts)
                .containsKey(VIDEO_ID_WITH_COMMENTS)
                .extractingByKey(VIDEO_ID_WITH_COMMENTS)
                .hasFieldOrPropertyWithValue("comments", 2L)
                .hasFieldOrPropertyWithValue("commentsAndReplies", 3L)
                .hasFieldOrPropertyWithValue("replies", 1L)
                .hasFieldOrPropertyWithValue("totalReplies", 4L);
    }

    @Test
    void getCommentCountsByVideoIds_emptyCount() {
        when(commentRepository.countCommentsForVideoIds(VIDEO_IDS)).thenReturn(COMMENT_COUNTS);

        var commentCounts = commentService.getCommentCountsByVideoIds(VIDEO_IDS);

        assertThat(commentCounts.get(VIDEO_ID_WITHOUT_COMMENTS))
                .hasFieldOrPropertyWithValue("comments", 0L)
                .hasFieldOrPropertyWithValue("commentsAndReplies", 0L)
                .hasFieldOrPropertyWithValue("replies", 0L)
                .hasFieldOrPropertyWithValue("totalReplies", 0L);
    }
}
