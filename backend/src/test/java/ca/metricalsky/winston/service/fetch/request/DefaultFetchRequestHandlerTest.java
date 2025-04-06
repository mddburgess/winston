package ca.metricalsky.winston.service.fetch.request;

import ca.metricalsky.winston.entity.fetch.FetchAction;
import ca.metricalsky.winston.entity.fetch.FetchRequest;
import ca.metricalsky.winston.entity.fetch.FetchRequest.FetchType;
import ca.metricalsky.winston.events.SsePublisher;
import ca.metricalsky.winston.exception.AppException;
import ca.metricalsky.winston.service.fetch.FetchRequestService;
import ca.metricalsky.winston.service.fetch.action.FetchActionHandler;
import ca.metricalsky.winston.service.fetch.action.FetchActionHandlerFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultFetchRequestHandlerTest {

    @InjectMocks
    private DefaultFetchRequestHandler defaultFetchRequestHandler;

    @Mock
    private FetchActionHandlerFactory fetchActionHandlerFactory;
    @Mock
    private FetchRequestService fetchRequestService;
    @Mock
    private FetchActionHandler fetchActionHandler;
    @Mock
    private SsePublisher ssePublisher;

    @Test
    void fetch() {
        var fetchRequest = FetchRequest.builder()
                .fetchType(FetchType.CHANNELS)
                .build();

        when(fetchRequestService.startFetch(fetchRequest))
                .thenReturn(fetchRequest);
        when(fetchActionHandlerFactory.getHandlerForAction(any(FetchAction.class)))
                .thenReturn(fetchActionHandler);

        defaultFetchRequestHandler.fetch(fetchRequest, ssePublisher);

        verify(fetchRequestService).fetchCompleted(fetchRequest);
    }

    @Test
    void fetch_exception() {
        var fetchRequest = FetchRequest.builder()
                .fetchType(FetchType.CHANNELS)
                .build();
        var appException = new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "");

        when(fetchRequestService.startFetch(fetchRequest))
                .thenReturn(fetchRequest);
        when(fetchActionHandlerFactory.getHandlerForAction(any(FetchAction.class)))
                .thenReturn(fetchActionHandler);
        when(fetchActionHandler.fetch(any(FetchAction.class), eq(ssePublisher)))
                .thenThrow(appException);

        assertThatThrownBy(() -> defaultFetchRequestHandler.fetch(fetchRequest, ssePublisher))
                .isEqualTo(appException);

        verify(fetchRequestService).fetchFailed(fetchRequest, appException);
    }
}
