package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.dto.fetch.FetchRequestDto;
import ca.metricalsky.winston.entity.fetch.FetchAction;
import ca.metricalsky.winston.entity.fetch.FetchRequest;
import ca.metricalsky.winston.events.FetchEvent;
import ca.metricalsky.winston.events.PublisherException;
import ca.metricalsky.winston.mapper.entity.FetchRequestMapper;
import ca.metricalsky.winston.events.SsePublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AsyncFetchServiceTest {

    @InjectMocks
    private AsyncFetchService asyncFetchService;

    @Mock
    private FetchActionHandler<Object> fetchActionHandler;
    @Mock
    private FetchActionHandlerFactory fetchActionHandlerFactory;
    @Mock
    private FetchActionService fetchActionService;
    @Mock
    private FetchRequestMapper fetchRequestMapper;
    @Mock
    private FetchRequestService fetchRequestService;
    @Mock
    private SsePublisher ssePublisher;

    @Test
    void fetch() {
        var fetchRequestDto = new FetchRequestDto();
        var fetchRequest = FetchRequest.builder()
                .id(1L)
                .fetchType(FetchRequest.FetchType.CHANNELS)
                .objectId("objectId")
                .build();
        var fetchAction = FetchAction.builder()
                .fetchRequestId(1L)
                .actionType(FetchAction.ActionType.CHANNELS)
                .objectId("objectId")
                .status(FetchAction.Status.FETCHING)
                .build();
        var fetchResult = new FetchResult<>(fetchAction, new Object(), null);

        when(fetchRequestMapper.toFetchRequest(fetchRequestDto))
                .thenReturn(fetchRequest);
        when(fetchRequestService.startFetch(fetchRequest))
                .thenReturn(fetchRequest);
        when(fetchActionService.actionFetching(any(FetchAction.class)))
                .thenReturn(fetchAction);
        when(fetchActionHandlerFactory.getHandlerForAction(fetchAction))
                .thenReturn((FetchActionHandler) fetchActionHandler);
        when(fetchActionHandler.fetch(fetchAction))
                .thenReturn(fetchResult);

        asyncFetchService.fetch(fetchRequestDto, ssePublisher);

        verify(fetchActionService).actionCompleted(fetchAction, 1);
        verify(ssePublisher).publish(any(FetchEvent.class));
        verify(fetchRequestService).fetchCompleted(fetchRequest);
        verify(ssePublisher).complete();
    }

    @Test
    void fetch_publisherException() {
        var fetchRequestDto = new FetchRequestDto();
        var fetchRequest = FetchRequest.builder()
                .id(1L)
                .fetchType(FetchRequest.FetchType.CHANNELS)
                .objectId("objectId")
                .build();
        var fetchAction = FetchAction.builder()
                .fetchRequestId(1L)
                .actionType(FetchAction.ActionType.CHANNELS)
                .objectId("objectId")
                .status(FetchAction.Status.FETCHING)
                .build();
        var fetchResult = new FetchResult<>(fetchAction, new Object(), null);

        when(fetchRequestMapper.toFetchRequest(fetchRequestDto))
                .thenReturn(fetchRequest);
        when(fetchRequestService.startFetch(fetchRequest))
                .thenReturn(fetchRequest);
        when(fetchActionService.actionFetching(any(FetchAction.class)))
                .thenReturn(fetchAction);
        when(fetchActionHandlerFactory.getHandlerForAction(fetchAction))
                .thenReturn((FetchActionHandler) fetchActionHandler);
        when(fetchActionHandler.fetch(fetchAction))
                .thenReturn(fetchResult);
        doThrow(PublisherException.class)
                .when(ssePublisher).publish(any(FetchEvent.class));

        asyncFetchService.fetch(fetchRequestDto, ssePublisher);

        verify(fetchActionService).actionCompleted(fetchAction, 1);
        verify(ssePublisher).publish(any(FetchEvent.class));
        verify(fetchRequestService).fetchCompleted(fetchRequest);
    }

    @Test
    void fetch_publisherException_hasNextFetchAction() {
        var fetchRequestDto = new FetchRequestDto();
        var fetchRequest = FetchRequest.builder()
                .id(1L)
                .fetchType(FetchRequest.FetchType.CHANNELS)
                .objectId("objectId")
                .build();
        var fetchAction = FetchAction.builder()
                .fetchRequestId(1L)
                .actionType(FetchAction.ActionType.CHANNELS)
                .objectId("objectId")
                .status(FetchAction.Status.FETCHING)
                .build();
        var nextFetchAction = new FetchAction();
        var fetchResult = new FetchResult<>(fetchAction, new Object(), nextFetchAction);

        when(fetchRequestMapper.toFetchRequest(fetchRequestDto))
                .thenReturn(fetchRequest);
        when(fetchRequestService.startFetch(fetchRequest))
                .thenReturn(fetchRequest);
        when(fetchActionService.actionFetching(any(FetchAction.class)))
                .thenReturn(fetchAction);
        when(fetchActionHandlerFactory.getHandlerForAction(fetchAction))
                .thenReturn((FetchActionHandler) fetchActionHandler);
        when(fetchActionHandler.fetch(fetchAction))
                .thenReturn(fetchResult);
        doThrow(PublisherException.class)
                .when(ssePublisher).publish(any(FetchEvent.class));

        asyncFetchService.fetch(fetchRequestDto, ssePublisher);

        verify(fetchActionService).actionCompleted(fetchAction, 1);
        verify(ssePublisher).publish(any(FetchEvent.class));
        verify(fetchActionService).actionReady(nextFetchAction);
        verify(fetchRequestService).fetchFailed(eq(fetchRequest), any(PublisherException.class));
    }
}
