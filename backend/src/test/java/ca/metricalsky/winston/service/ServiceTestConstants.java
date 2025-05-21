package ca.metricalsky.winston.service;

import ca.metricalsky.winston.entity.view.CommentCountView;

import java.util.List;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

public final class ServiceTestConstants {

    public static final String VIDEO_ID_WITH_COMMENTS = "videoIdWithComments";
    public static final String VIDEO_ID_WITHOUT_COMMENTS = "videoIdWithoutComments";
    public static final List<String> VIDEO_IDS = List.of(VIDEO_ID_WITH_COMMENTS, VIDEO_ID_WITHOUT_COMMENTS);
    public static final List<CommentCountView> COMMENT_COUNTS = List.of(buildCommentCount());

    private ServiceTestConstants() {

    }

    public static CommentCountView buildCommentCount() {
        var commentCount = mock(CommentCountView.class);
        lenient().when(commentCount.getVideoId()).thenReturn(VIDEO_ID_WITH_COMMENTS);
        lenient().when(commentCount.getComments()).thenCallRealMethod();
        lenient().when(commentCount.getCommentsAndReplies()).thenReturn(3L);
        lenient().when(commentCount.getReplies()).thenReturn(1L);
        lenient().when(commentCount.getTotalReplies()).thenReturn(4L);
        return commentCount;
    }
}
