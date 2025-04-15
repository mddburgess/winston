package ca.metricalsky.winston.service.fetch.action;

import ca.metricalsky.winston.client.YouTubeClientAdapter;
import ca.metricalsky.winston.entity.Comment;
import ca.metricalsky.winston.entity.fetch.FetchAction;
import ca.metricalsky.winston.entity.fetch.FetchAction.ActionType;
import ca.metricalsky.winston.events.FetchDataEvent;
import ca.metricalsky.winston.events.FetchStatus;
import ca.metricalsky.winston.events.SsePublisher;
import ca.metricalsky.winston.service.CommentService;
import ca.metricalsky.winston.service.fetch.FetchActionService;
import com.google.api.services.youtube.model.CommentListResponse;
import com.google.api.services.youtube.model.CommentSnippet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FetchRepliesActionHandlerTest {

    private static final String VIDEO_ID = "videoId";
    private static final String COMMENT_ID = "commentId";
    private static final String REPLY_ID = "replyId";

    @InjectMocks
    private FetchRepliesActionHandler fetchRepliesActionHandler;

    @Mock
    private FetchActionService fetchActionService;
    @Mock
    private CommentService commentService;
    @Mock
    private YouTubeClientAdapter youTubeClientAdapter;
    @Mock
    private SsePublisher ssePublisher;
    @Captor
    private ArgumentCaptor<FetchDataEvent> fetchDataEvent;

    @Test
    void fetch() {
        var fetchAction = FetchAction.builder()
                .actionType(ActionType.REPLIES)
                .objectId(COMMENT_ID)
                .build();
        var commentListResponse = buildCommentListResponse();
        var topLevelComment = buildTopLevelComment();

        when(fetchActionService.actionFetching(fetchAction))
                .thenReturn(fetchAction);
        when(youTubeClientAdapter.getReplies(fetchAction))
                .thenReturn(commentListResponse);
        when(commentService.findById(COMMENT_ID))
                .thenReturn(Optional.of(topLevelComment));

        var nextFetchAction = fetchRepliesActionHandler.fetch(fetchAction, ssePublisher);

        assertThat(nextFetchAction).isNull();

        verify(commentService).saveAll(anyList());
        verify(fetchActionService).actionCompleted(fetchAction, commentListResponse.getItems().size());
        verify(ssePublisher).publish(fetchDataEvent.capture());

        assertThat(fetchDataEvent.getValue())
                .hasFieldOrPropertyWithValue("objectId", COMMENT_ID);
        assertThat(fetchDataEvent.getValue().items())
                .hasSize(commentListResponse.getItems().size())
                .first()
                .hasFieldOrPropertyWithValue("id", REPLY_ID)
                .hasFieldOrPropertyWithValue("videoId", VIDEO_ID);
    }

    private static CommentListResponse buildCommentListResponse() {
        var commentListResponse = new CommentListResponse();
        commentListResponse.setItems(List.of(buildReply()));
        return commentListResponse;
    }

    private static com.google.api.services.youtube.model.Comment buildReply() {
        var snippet = new CommentSnippet();
        snippet.setParentId(COMMENT_ID);

        var comment = new com.google.api.services.youtube.model.Comment();
        comment.setId(REPLY_ID);
        comment.setSnippet(snippet);
        return comment;
    }

    private static Comment buildTopLevelComment() {
        var comment = new Comment();
        comment.setId(COMMENT_ID);
        comment.setVideoId(VIDEO_ID);
        return comment;
    }
}
