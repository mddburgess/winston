package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.client.CommentsDisabledException;
import ca.metricalsky.winston.client.YouTubeClientAdapter;
import ca.metricalsky.winston.entity.Video;
import ca.metricalsky.winston.entity.fetch.FetchAction;
import ca.metricalsky.winston.repository.VideoRepository;
import ca.metricalsky.winston.service.CommentService;
import com.google.api.services.youtube.model.Comment;
import com.google.api.services.youtube.model.CommentThread;
import com.google.api.services.youtube.model.CommentThreadListResponse;
import com.google.api.services.youtube.model.CommentThreadSnippet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FetchCommentsActionHandlerTest {

    private static final String VIDEO_ID = "videoId";
    private static final String COMMENT_ID = "commentId";

    @InjectMocks
    private FetchCommentsActionHandler fetchCommentsActionHandler;

    @Mock
    private CommentService commentService;
    @Mock
    private VideoRepository videoRepository;
    @Mock
    private YouTubeClientAdapter youTubeClientAdapter;

    @Test
    void fetch() {
        var fetchAction = FetchAction.builder()
                .actionType(FetchAction.ActionType.COMMENTS)
                .objectId(VIDEO_ID)
                .build();
        when(youTubeClientAdapter.getComments(fetchAction))
                .thenReturn(buildCommentThreadListResponse());

        var fetchResult = fetchCommentsActionHandler.fetch(fetchAction);

        assertThat(fetchResult)
                .hasFieldOrPropertyWithValue("actionType", FetchAction.ActionType.COMMENTS)
                .hasFieldOrPropertyWithValue("objectId", VIDEO_ID)
                .hasFieldOrPropertyWithValue("nextFetchAction", null);
        assertThat(fetchResult.items())
                .hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue("id", COMMENT_ID);

        verify(commentService).saveAll(anyList());
    }

    @Test
    void fetch_commentsDisabled() {
        var fetchAction = FetchAction.builder()
                .actionType(FetchAction.ActionType.COMMENTS)
                .objectId(VIDEO_ID)
                .build();
        var video = new Video();
        video.setId(VIDEO_ID);

        when(youTubeClientAdapter.getComments(fetchAction)).thenThrow(CommentsDisabledException.class);
        when(videoRepository.findById(VIDEO_ID)).thenReturn(Optional.of(video));

        assertThatThrownBy(() -> fetchCommentsActionHandler.fetch(fetchAction))
                .isExactlyInstanceOf(CommentsDisabledException.class);

        assertThat(video.getCommentsDisabled()).isTrue();
        verify(videoRepository).save(video);
    }

    private static CommentThreadListResponse buildCommentThreadListResponse() {
        var commentThreadListResponse = new CommentThreadListResponse();
        commentThreadListResponse.setItems(List.of(buildCommentThread()));
        return commentThreadListResponse;
    }

    private static CommentThread buildCommentThread() {
        var snippet = new CommentThreadSnippet();
        snippet.setTopLevelComment(buildTopLevelComment());

        var commentThread = new CommentThread();
        commentThread.setSnippet(snippet);
        return commentThread;
    }

    private static Comment buildTopLevelComment() {
        var comment = new Comment();
        comment.setId(COMMENT_ID);
        return comment;
    }
}
