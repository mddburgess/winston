package ca.metricalsky.winston.service.fetch.request;

import ca.metricalsky.winston.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.entity.fetch.FetchRequestEntity;
import ca.metricalsky.winston.entity.fetch.FetchRequestEntity.FetchType;
import ca.metricalsky.winston.events.SsePublisher;
import ca.metricalsky.winston.exception.AppException;
import ca.metricalsky.winston.service.fetch.FetchRequestService;
import ca.metricalsky.winston.service.fetch.action.FetchChannelActionHandler;
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
class FetchChannelsRequestHandlerTest {

    @InjectMocks
    private FetchChannelsRequestHandler fetchChannelsRequestHandler;

    @Mock
    private FetchRequestService fetchRequestService;
    @Mock
    private FetchChannelActionHandler fetchChannelActionHandler;
    @Mock
    private SsePublisher ssePublisher;

    @Test
    void fetch() {
        var fetchRequest = FetchRequestEntity.builder()
                .fetchType(FetchType.CHANNELS)
                .build();

        when(fetchRequestService.startFetch(fetchRequest))
                .thenReturn(fetchRequest);

        fetchChannelsRequestHandler.fetch(fetchRequest, ssePublisher);

        verify(fetchRequestService).fetchCompleted(fetchRequest);
    }

    @Test
    void fetch_exception() {
        var fetchRequest = FetchRequestEntity.builder()
                .fetchType(FetchType.CHANNELS)
                .build();
        var appException = new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "");

        when(fetchRequestService.startFetch(fetchRequest))
                .thenReturn(fetchRequest);
        when(fetchChannelActionHandler.fetch(any(FetchActionEntity.class), eq(ssePublisher)))
                .thenThrow(appException);

        assertThatThrownBy(() -> fetchChannelsRequestHandler.fetch(fetchRequest, ssePublisher))
                .isEqualTo(appException);

        verify(fetchRequestService).fetchFailed(fetchRequest, appException);
    }
}
