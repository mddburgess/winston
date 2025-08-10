package ca.metricalsky.winston.service.fetch.operation;

import ca.metricalsky.winston.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.entity.fetch.FetchOperationEntity;
import ca.metricalsky.winston.events.SsePublisher;
import ca.metricalsky.winston.exception.AppException;
import ca.metricalsky.winston.repository.CommentRepository;
import ca.metricalsky.winston.service.VideoCommentsService;
import ca.metricalsky.winston.service.fetch.FetchOperationService;
import ca.metricalsky.winston.service.fetch.action.FetchRepliesActionHandler;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Disabled
@ExtendWith(MockitoExtension.class)
class FetchVideoRepliesRequestHandlerTest {

    @InjectMocks
    private FetchVideoRepliesOperationHandler fetchVideoRepliesRequestHandler;

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private FetchOperationService fetchOperationService;
    @Mock
    private FetchRepliesActionHandler fetchRepliesActionHandler;
    @Mock
    private SsePublisher ssePublisher;
    @Mock
    private VideoCommentsService videoCommentsService;
    @Captor
    private ArgumentCaptor<FetchActionEntity> fetchAction;

    @Test
    void fetch() {
        var fetchRequest = FetchOperationEntity.builder()
                .objectId("videoId")
                .build();
        var commentIds = List.of("commentId1", "commentId2");

        when(fetchOperationService.startFetch(fetchRequest))
                .thenReturn(fetchRequest);
        when(commentRepository.findIdsMissingRepliesByVideoId(fetchRequest.getObjectId()))
                .thenReturn(commentIds);

        fetchVideoRepliesRequestHandler.fetch(fetchRequest, ssePublisher);

        verify(fetchRepliesActionHandler, times(2)).fetch(fetchAction.capture(), eq(ssePublisher));
        verify(fetchOperationService).fetchSuccessful(fetchRequest);
        verify(videoCommentsService).updateVideoComments(fetchRequest.getObjectId());

        var fetchActionValues = fetchAction.getAllValues();
        assertThat(fetchActionValues.getFirst())
                .hasFieldOrPropertyWithValue("objectId", "commentId1");
        assertThat(fetchActionValues.getLast())
                .hasFieldOrPropertyWithValue("objectId", "commentId2");
    }

    @Test
    void fetch_exception() {
        var fetchRequest = FetchOperationEntity.builder()
                .objectId("videoId")
                .build();
        var commentIds = List.of("commentId1", "commentId2");
        var appException = new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "");

        when(fetchOperationService.startFetch(fetchRequest))
                .thenReturn(fetchRequest);
        when(commentRepository.findIdsMissingRepliesByVideoId(fetchRequest.getObjectId()))
                .thenReturn(commentIds);
        when(fetchRepliesActionHandler.fetch(any(FetchActionEntity.class), eq(ssePublisher)))
                .thenThrow(appException);

        assertThatThrownBy(() -> fetchVideoRepliesRequestHandler.fetch(fetchRequest, ssePublisher))
                .isEqualTo(appException);

        verify(fetchOperationService).fetchFailed(fetchRequest, appException);
        verify(videoCommentsService).updateVideoComments(fetchRequest.getObjectId());
    }
}
