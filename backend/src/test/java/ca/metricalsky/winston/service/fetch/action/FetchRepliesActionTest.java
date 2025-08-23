package ca.metricalsky.winston.service.fetch.action;

import ca.metricalsky.winston.api.model.Comment;
import ca.metricalsky.winston.dao.CommentDataService;
import ca.metricalsky.winston.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.entity.fetch.FetchActionEntity.Type;
import ca.metricalsky.winston.events.FetchDataEvent;
import ca.metricalsky.winston.events.SsePublisher;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FetchRepliesActionTest {

    @InjectMocks
    private FetchRepliesAction fetchRepliesAction;

    @Mock
    private FetchActionService fetchActionService;
    @Mock
    private CommentDataService commentDataService;
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
                .actionType(Type.REPLIES)
                .objectId(TestUtils.randomId())
                .build();
        when(fetchActionService.actionFetching(fetchAction))
                .thenReturn(fetchAction);

        var commentListResponse = ClientTestObjectFactory.buildCommentListResponse();
        when(youTubeService.getReplies(fetchAction))
                .thenReturn(commentListResponse);

        var comment = new Comment();
        when(commentDataService.saveReplies(fetchAction.getObjectId(), commentListResponse))
                .thenReturn(List.of(comment));

        var nextFetchAction = fetchRepliesAction.fetch(fetchAction);

        assertThat(nextFetchAction)
                .as("nextFetchAction")
                .isNull();

        verify(fetchActionService).actionSuccessful(fetchAction, commentListResponse.getItems().size());

        assertThat(fetchDataEvent.getValue())
                .as("fetchDataEvent")
                .hasFieldOrPropertyWithValue("objectId", fetchAction.getObjectId());
        assertThat(fetchDataEvent.getValue().items())
                .as("fetchDataEvent.items")
                .hasSize(1)
                .first().isEqualTo(comment);
    }
}
