package ca.metricalsky.winston.service.fetch.request;

import ca.metricalsky.winston.entity.fetch.FetchAction;
import ca.metricalsky.winston.entity.fetch.FetchRequest;
import ca.metricalsky.winston.events.SsePublisher;
import ca.metricalsky.winston.exception.AppException;
import ca.metricalsky.winston.repository.CommentRepository;
import ca.metricalsky.winston.service.fetch.FetchRequestService;
import ca.metricalsky.winston.service.fetch.action.FetchActionHandler;
import ca.metricalsky.winston.service.fetch.action.FetchActionHandlerFactory;
import ca.metricalsky.winston.service.fetch.action.FetchRepliesActionHandler;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FetchVideoRepliesRequestHandlerTest {

    @InjectMocks
    private FetchVideoRepliesRequestHandler fetchVideoRepliesRequestHandler;

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private FetchActionHandlerFactory fetchActionHandlerFactory;
    @Mock
    private FetchRequestService fetchRequestService;
    @Mock
    private FetchRepliesActionHandler fetchRepliesActionHandler;
    @Mock
    private SsePublisher ssePublisher;
    @Captor
    private ArgumentCaptor<FetchAction> fetchAction;

    @Test
    void fetch() {
        var fetchRequest = FetchRequest.builder()
                .objectId("videoId")
                .build();
        var commentIds = List.of("commentId1", "commentId2");

        when(fetchRequestService.startFetch(fetchRequest))
                .thenReturn(fetchRequest);
        when(commentRepository.findIdsMissingRepliesByVideoId(fetchRequest.getObjectId()))
                .thenReturn(commentIds);
        when(fetchActionHandlerFactory.getHandlerForAction(any(FetchAction.class)))
                .thenReturn((FetchActionHandler) fetchRepliesActionHandler);

        fetchVideoRepliesRequestHandler.fetch(fetchRequest, ssePublisher);

        verify(fetchRepliesActionHandler, times(2)).fetch(fetchAction.capture(), eq(ssePublisher));
        verify(fetchRequestService).fetchCompleted(fetchRequest);

        var fetchActionValues = fetchAction.getAllValues();
        assertThat(fetchActionValues.getFirst())
                .hasFieldOrPropertyWithValue("objectId", "commentId1");
        assertThat(fetchActionValues.getLast())
                .hasFieldOrPropertyWithValue("objectId", "commentId2");
    }

    @Test
    void fetch_exception() {
        var fetchRequest = FetchRequest.builder()
                .objectId("videoId")
                .build();
        var commentIds = List.of("commentId1", "commentId2");
        var appException = new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "");

        when(fetchRequestService.startFetch(fetchRequest))
                .thenReturn(fetchRequest);
        when(commentRepository.findIdsMissingRepliesByVideoId(fetchRequest.getObjectId()))
                .thenReturn(commentIds);
        when(fetchActionHandlerFactory.getHandlerForAction(any(FetchAction.class)))
                .thenReturn((FetchActionHandler) fetchRepliesActionHandler);
        when(fetchRepliesActionHandler.fetch(any(FetchAction.class), eq(ssePublisher)))
                .thenThrow(appException);

        assertThatThrownBy(() -> fetchVideoRepliesRequestHandler.fetch(fetchRequest, ssePublisher))
                .isEqualTo(appException);

        verify(fetchRequestService).fetchFailed(fetchRequest, appException);
    }
}
