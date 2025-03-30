package ca.metricalsky.winston.service;

import ca.metricalsky.winston.entity.view.CommentCount;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class ServiceTestConstants {

    public static final String VIDEO_ID_WITH_COMMENTS = "videoIdWithComments";
    public static final String VIDEO_ID_WITHOUT_COMMENTS = "videoIdWithoutComments";
    public static final List<String> VIDEO_IDS = List.of(VIDEO_ID_WITH_COMMENTS, VIDEO_ID_WITHOUT_COMMENTS);
    public static final List<CommentCount> COMMENT_COUNTS = List.of(buildCommentCount());

    private ServiceTestConstants() {

    }

    private static CommentCount buildCommentCount() {
        var commentCount = mock(CommentCount.class);
        when(commentCount.getVideoId()).thenReturn(VIDEO_ID_WITH_COMMENTS);
        when(commentCount.getComments()).thenCallRealMethod();
        when(commentCount.getCommentsAndReplies()).thenReturn(3L);
        when(commentCount.getReplies()).thenReturn(1L);
        when(commentCount.getTotalReplies()).thenReturn(4L);
        return commentCount;
    }
}
