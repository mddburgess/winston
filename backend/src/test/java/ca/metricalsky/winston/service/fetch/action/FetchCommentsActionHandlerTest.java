package ca.metricalsky.winston.service.fetch.action;

import ca.metricalsky.winston.client.CommentsDisabledException;
import ca.metricalsky.winston.client.YouTubeClientAdapter;
import ca.metricalsky.winston.entity.fetch.FetchAction;
import ca.metricalsky.winston.events.FetchDataEvent;
import ca.metricalsky.winston.events.SsePublisher;
import ca.metricalsky.winston.service.CommentService;
import ca.metricalsky.winston.service.VideoCommentsService;
import ca.metricalsky.winston.service.fetch.FetchActionService;
import com.google.api.services.youtube.model.Comment;
import com.google.api.services.youtube.model.CommentThread;
import com.google.api.services.youtube.model.CommentThreadListResponse;
import com.google.api.services.youtube.model.CommentThreadSnippet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FetchCommentsActionHandlerTest {

    private static final String VIDEO_ID = "videoId";
    private static final String COMMENT_ID = "commentId";

    @InjectMocks
    private FetchCommentsActionHandler fetchCommentsActionHandler;

    @Mock
    private FetchActionService fetchActionService;
    @Mock
    private CommentService commentService;
    @Mock
    private VideoCommentsService videoCommentsService;
    @Mock
    private YouTubeClientAdapter youTubeClientAdapter;
    @Mock
    private SsePublisher ssePublisher;
    @Captor
    private ArgumentCaptor<FetchDataEvent> fetchDataEvent;

    @Test
    void fetch() {
        var fetchAction = FetchAction.builder()
                .actionType(FetchAction.ActionType.COMMENTS)
                .objectId(VIDEO_ID)
                .build();
        var commentThreadListResponse = buildCommentThreadListResponse();

        when(fetchActionService.actionFetching(fetchAction))
                .thenReturn(fetchAction);
        when(youTubeClientAdapter.getComments(fetchAction))
                .thenReturn(commentThreadListResponse);

        var nextFetchAction = fetchCommentsActionHandler.fetch(fetchAction, ssePublisher);

        assertThat(nextFetchAction).isNull();

        verify(commentService).saveAll(anyList());
        verify(fetchActionService).actionCompleted(fetchAction, commentThreadListResponse.getItems().size());
        verify(ssePublisher).publish(fetchDataEvent.capture());

        assertThat(fetchDataEvent.getValue())
                .hasFieldOrPropertyWithValue("objectId", VIDEO_ID);
        assertThat(fetchDataEvent.getValue().items())
                .hasSize(commentThreadListResponse.getItems().size())
                .first()
                .hasFieldOrPropertyWithValue("id", COMMENT_ID);
    }

    @Test
    void fetch_commentsDisabled() {
        var fetchAction = FetchAction.builder()
                .actionType(FetchAction.ActionType.COMMENTS)
                .objectId(VIDEO_ID)
                .build();

        when(fetchActionService.actionFetching(fetchAction))
                .thenReturn(fetchAction);
        when(youTubeClientAdapter.getComments(fetchAction))
                .thenThrow(new CommentsDisabledException(null));

        var exception = catchThrowableOfType(CommentsDisabledException.class,
                () -> fetchCommentsActionHandler.fetch(fetchAction, ssePublisher));

        assertThat(exception)
                .hasFieldOrPropertyWithValue("status", HttpStatus.UNPROCESSABLE_ENTITY)
                .hasMessageEndingWith("Comments are disabled for the requested video.");

        verify(videoCommentsService).markVideoCommentsDisabled(VIDEO_ID);
        verify(fetchActionService).actionFailed(fetchAction, exception);
        verifyNoInteractions(ssePublisher);
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
