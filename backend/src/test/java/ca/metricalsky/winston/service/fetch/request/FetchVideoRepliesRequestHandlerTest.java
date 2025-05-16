package ca.metricalsky.winston.service.fetch.request;

import ca.metricalsky.winston.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.entity.fetch.FetchRequestEntity;
import ca.metricalsky.winston.events.SsePublisher;
import ca.metricalsky.winston.exception.AppException;
import ca.metricalsky.winston.repository.CommentRepository;
import ca.metricalsky.winston.service.VideoCommentsService;
import ca.metricalsky.winston.service.fetch.FetchRequestService;
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
    private FetchRequestService fetchRequestService;
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
        var fetchRequest = FetchRequestEntity.builder()
                .objectId("videoId")
                .build();
        var commentIds = List.of("commentId1", "commentId2");

        when(fetchRequestService.startFetch(fetchRequest))
                .thenReturn(fetchRequest);
        when(commentRepository.findIdsMissingRepliesByVideoId(fetchRequest.getObjectId()))
                .thenReturn(commentIds);

        fetchVideoRepliesRequestHandler.fetch(fetchRequest, ssePublisher);

        verify(fetchRepliesActionHandler, times(2)).fetch(fetchAction.capture(), eq(ssePublisher));
        verify(fetchRequestService).fetchCompleted(fetchRequest);
        verify(videoCommentsService).updateVideoComments(fetchRequest.getObjectId());

        var fetchActionValues = fetchAction.getAllValues();
        assertThat(fetchActionValues.getFirst())
                .hasFieldOrPropertyWithValue("objectId", "commentId1");
        assertThat(fetchActionValues.getLast())
                .hasFieldOrPropertyWithValue("objectId", "commentId2");
    }

    @Test
    void fetch_exception() {
        var fetchRequest = FetchRequestEntity.builder()
                .objectId("videoId")
                .build();
        var commentIds = List.of("commentId1", "commentId2");
        var appException = new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "");

        when(fetchRequestService.startFetch(fetchRequest))
                .thenReturn(fetchRequest);
        when(commentRepository.findIdsMissingRepliesByVideoId(fetchRequest.getObjectId()))
                .thenReturn(commentIds);
        when(fetchRepliesActionHandler.fetch(any(FetchActionEntity.class), eq(ssePublisher)))
                .thenThrow(appException);

        assertThatThrownBy(() -> fetchVideoRepliesRequestHandler.fetch(fetchRequest, ssePublisher))
                .isEqualTo(appException);

        verify(fetchRequestService).fetchFailed(fetchRequest, appException);
        verify(videoCommentsService).updateVideoComments(fetchRequest.getObjectId());
    }
}
