package ca.metricalsky.winston.service.fetch.action;

import ca.metricalsky.winston.api.model.TopLevelComment;
import ca.metricalsky.winston.client.CommentsDisabledException;
import ca.metricalsky.winston.dao.CommentDataService;
import ca.metricalsky.winston.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.events.FetchDataEvent;
import ca.metricalsky.winston.events.SsePublisher;
import ca.metricalsky.winston.service.VideoCommentsService;
import ca.metricalsky.winston.service.YouTubeService;
import ca.metricalsky.winston.service.fetch.FetchActionService;
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
    private CommentDataService commentDataService;
    @Mock
    private VideoCommentsService videoCommentsService;
    @Mock
    private YouTubeService youTubeService;
    @Mock
    private SsePublisher ssePublisher;
    @Captor
    private ArgumentCaptor<FetchDataEvent> fetchDataEvent;

    @Test
    void fetch() {
        var fetchAction = FetchActionEntity.builder()
                .actionType(FetchActionEntity.Type.COMMENTS)
                .objectId(VIDEO_ID)
                .build();
        when(fetchActionService.actionFetching(fetchAction))
                .thenReturn(fetchAction);

        var commentThreadListResponse = buildCommentThreadListResponse();
        when(youTubeService.getComments(fetchAction))
                .thenReturn(commentThreadListResponse);

        var comment = new TopLevelComment();
        when(commentDataService.saveComments(commentThreadListResponse))
                .thenReturn(List.of(comment));

        var nextFetchAction = fetchCommentsActionHandler.fetch(fetchAction, ssePublisher);

        assertThat(nextFetchAction)
                .as("nextFetchAction")
                .isNull();

        verify(fetchActionService).actionSuccessful(fetchAction, commentThreadListResponse.getItems().size());
        verify(ssePublisher).publish(fetchDataEvent.capture());

        assertThat(fetchDataEvent.getValue())
                .as("fetchDataEvent")
                .hasFieldOrPropertyWithValue("objectId", fetchAction.getObjectId());
        assertThat(fetchDataEvent.getValue().items())
                .as("fetchDataEvent.items")
                .hasSize(1)
                .first().isEqualTo(comment);
    }

    @Test
    void fetch_commentsDisabled() {
        var fetchAction = FetchActionEntity.builder()
                .actionType(FetchActionEntity.Type.COMMENTS)
                .objectId(VIDEO_ID)
                .build();
        when(fetchActionService.actionFetching(fetchAction))
                .thenReturn(fetchAction);

        when(youTubeService.getComments(fetchAction))
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
        var comment = new com.google.api.services.youtube.model.Comment();
        comment.setId(COMMENT_ID);

        var snippet = new CommentThreadSnippet();
        snippet.setTopLevelComment(comment);

        var commentThread = new CommentThread();
        commentThread.setSnippet(snippet);
        return commentThread;
    }
}
