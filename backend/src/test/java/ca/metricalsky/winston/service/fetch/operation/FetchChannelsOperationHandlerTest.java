package ca.metricalsky.winston.service.fetch.operation;

import ca.metricalsky.winston.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.entity.fetch.FetchOperationEntity;
import ca.metricalsky.winston.entity.fetch.FetchOperationEntity.Type;
import ca.metricalsky.winston.events.SsePublisher;
import ca.metricalsky.winston.exception.AppException;
import ca.metricalsky.winston.service.fetch.FetchOperationService;
import ca.metricalsky.winston.service.fetch.action.FetchChannelActionHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FetchChannelsOperationHandlerTest {

    @InjectMocks
    private FetchChannelsOperationHandler fetchChannelsOperationHandler;

    @Mock
    private FetchOperationService fetchOperationService;
    @Mock
    private FetchChannelActionHandler fetchChannelActionHandler;
    @Mock
    private SsePublisher ssePublisher;

    @Test
    void fetch() {
        var operation = FetchOperationEntity.builder()
                .operationType(Type.CHANNELS)
                .build();

        when(fetchOperationService.startFetch(operation))
                .thenReturn(operation);

        fetchChannelsOperationHandler.fetch(operation);

        verify(fetchOperationService).fetchSuccessful(operation);
    }

    @Test
    void fetch_exception() {
        var operation = FetchOperationEntity.builder()
                .operationType(Type.CHANNELS)
                .build();
        var appException = new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "");

        when(fetchOperationService.startFetch(operation))
                .thenReturn(operation);
        when(fetchChannelActionHandler.fetch(any(FetchActionEntity.class)))
                .thenThrow(appException);

        assertThatThrownBy(() -> fetchChannelsOperationHandler.fetch(operation))
                .isEqualTo(appException);

        verify(fetchOperationService).fetchFailed(operation, appException);
    }
}
