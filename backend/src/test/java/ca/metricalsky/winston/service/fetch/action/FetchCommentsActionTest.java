package ca.metricalsky.winston.service.fetch.action;

import ca.metricalsky.winston.api.model.TopLevelComment;
import ca.metricalsky.winston.client.CommentsDisabledException;
import ca.metricalsky.winston.dao.CommentDataService;
import ca.metricalsky.winston.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.events.FetchDataEvent;
import ca.metricalsky.winston.events.SsePublisher;
import ca.metricalsky.winston.exception.FetchOperationException;
import ca.metricalsky.winston.service.VideoCommentsService;
import ca.metricalsky.winston.service.YouTubeService;
import ca.metricalsky.winston.service.fetch.FetchActionService;
import ca.metricalsky.winston.service.fetch.FetchResult;
import ca.metricalsky.winston.test.ClientTestObjectFactory;
import ca.metricalsky.winston.test.TestUtils;
import org.junit.jupiter.api.Disabled;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FetchCommentsActionTest {

    @InjectMocks
    private FetchCommentsAction fetchCommentsAction;

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
    @Disabled
    void fetch() {
        var fetchAction = FetchActionEntity.builder()
                .actionType(FetchActionEntity.Type.COMMENTS)
                .objectId(TestUtils.randomId())
                .build();
        when(fetchActionService.actionFetching(fetchAction))
                .thenReturn(fetchAction);

        var commentThreadListResponse = ClientTestObjectFactory.buildCommentThreadListResponse();
        when(youTubeService.getComments(fetchAction))
                .thenReturn(commentThreadListResponse);

        var comment = new TopLevelComment();
        when(commentDataService.saveComments(commentThreadListResponse))
                .thenReturn(List.of(comment));

        var nextFetchAction = fetchCommentsAction.fetch(fetchAction);

        assertThat(nextFetchAction)
                .as("nextFetchAction")
                .isNull();

        verify(fetchActionService).actionSuccessful(fetchAction, commentThreadListResponse.getItems().size());

        assertThat(fetchDataEvent.getValue())
                .as("fetchDataEvent")
                .hasFieldOrPropertyWithValue("objectId", fetchAction.getObjectId());
        assertThat(fetchDataEvent.getValue().items())
                .as("fetchDataEvent.items")
                .hasSize(1)
                .first().isEqualTo(comment);
    }

    @Test
    @Disabled
    void fetch_commentsDisabled() {
        var fetchAction = FetchActionEntity.builder()
                .actionType(FetchActionEntity.Type.COMMENTS)
                .objectId(TestUtils.randomId())
                .build();
        when(fetchActionService.actionFetching(fetchAction))
                .thenReturn(fetchAction);

        when(youTubeService.getComments(fetchAction))
                .thenThrow(new CommentsDisabledException(null));

        var exception = catchThrowableOfType(FetchOperationException.class,
                () -> fetchCommentsAction.fetch(fetchAction));

        assertThat(exception).cause()
                .isExactlyInstanceOf(CommentsDisabledException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.UNPROCESSABLE_ENTITY)
                .hasMessageEndingWith("Comments are disabled for the requested video.");

        verify(videoCommentsService).markVideoCommentsDisabled(fetchAction.getObjectId());
        verify(fetchActionService).actionFailed(fetchAction, exception);
        verifyNoInteractions(ssePublisher);
    }
}
