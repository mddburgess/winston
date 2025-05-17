package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.dto.fetch.FetchRequest;
import ca.metricalsky.winston.entity.fetch.FetchOperationEntity;
import ca.metricalsky.winston.entity.fetch.FetchOperationEntity.Type;
import ca.metricalsky.winston.entity.fetch.FetchRequestEntity;
import ca.metricalsky.winston.events.FetchStatusEvent;
import ca.metricalsky.winston.events.PublisherException;
import ca.metricalsky.winston.events.SsePublisher;
import ca.metricalsky.winston.exception.AppException;
import ca.metricalsky.winston.mapper.entity.FetchRequestMapper;
import ca.metricalsky.winston.service.fetch.request.FetchOperationHandler;
import ca.metricalsky.winston.service.fetch.request.FetchOperationHandlerFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FetchServiceTest {

    @InjectMocks
    private FetchService fetchService;

    @Mock
    private FetchOperationHandler fetchOperationHandler;
    @Mock
    private FetchOperationHandlerFactory fetchOperationHandlerFactory;
    @Mock
    private FetchRequestMapper fetchRequestMapper;
    @Mock
    private SsePublisher ssePublisher;

    @Test
    void fetchAsync() {
        var fetchRequestDto = new FetchRequest();
        var fetchRequest = buildFetchRequestEntity();

        when(fetchRequestMapper.toFetchRequest(fetchRequestDto))
                .thenReturn(fetchRequest);
        when(fetchOperationHandlerFactory.getHandler(fetchRequest.getOperations().getFirst()))
                .thenReturn(fetchOperationHandler);

        fetchService.fetchAsync(fetchRequestDto, ssePublisher);

        verify(fetchOperationHandler).fetch(fetchRequest.getOperations().getFirst(), ssePublisher);
        verify(ssePublisher).complete();
    }

    @Test
    void fetchAsync_actionFailed() {
        var fetchRequestDto = new FetchRequest();
        var fetchRequest = buildFetchRequestEntity();
        var exception = new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "");

        when(fetchRequestMapper.toFetchRequest(fetchRequestDto))
                .thenReturn(fetchRequest);
        when(fetchOperationHandlerFactory.getHandler(fetchRequest.getOperations().getFirst()))
                .thenReturn(fetchOperationHandler);
        doThrow(exception)
                .when(fetchOperationHandler).fetch(fetchRequest.getOperations().getFirst(), ssePublisher);
        when(ssePublisher.isOpen())
                .thenReturn(true);

        fetchService.fetchAsync(fetchRequestDto, ssePublisher);

        verify(ssePublisher).publish(any(FetchStatusEvent.class));
        verify(ssePublisher).completeWithError(exception);
    }

    @Test
    void fetchAsync_publisherClosed() {
        var fetchRequestDto = new FetchRequest();
        var fetchRequest = buildFetchRequestEntity();
        var exception = new PublisherException("");

        when(fetchRequestMapper.toFetchRequest(fetchRequestDto))
                .thenReturn(fetchRequest);
        when(fetchOperationHandlerFactory.getHandler(fetchRequest.getOperations().getFirst()))
                .thenReturn(fetchOperationHandler);
        doThrow(exception)
                .when(fetchOperationHandler).fetch(fetchRequest.getOperations().getFirst(), ssePublisher);
        when(ssePublisher.isOpen())
                .thenReturn(false);

        fetchService.fetchAsync(fetchRequestDto, ssePublisher);

        verifyNoMoreInteractions(ssePublisher);
    }

    private static FetchRequestEntity buildFetchRequestEntity() {
        var fetchOperationEntity = FetchOperationEntity.builder()
                .operationType(Type.CHANNELS)
                .build();
        return FetchRequestEntity.builder()
                .operations(List.of(fetchOperationEntity))
                .build();
    }
}
