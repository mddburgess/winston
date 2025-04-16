package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.dto.fetch.FetchRequestDto;
import ca.metricalsky.winston.entity.fetch.FetchRequest;
import ca.metricalsky.winston.entity.fetch.FetchRequest.FetchType;
import ca.metricalsky.winston.events.FetchStatusEvent;
import ca.metricalsky.winston.events.PublisherException;
import ca.metricalsky.winston.events.SsePublisher;
import ca.metricalsky.winston.exception.AppException;
import ca.metricalsky.winston.mapper.entity.FetchRequestMapper;
import ca.metricalsky.winston.service.fetch.request.FetchRequestHandler;
import ca.metricalsky.winston.service.fetch.request.FetchRequestHandlerFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

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
    private FetchRequestHandler fetchRequestHandler;
    @Mock
    private FetchRequestHandlerFactory fetchRequestHandlerFactory;
    @Mock
    private FetchRequestMapper fetchRequestMapper;
    @Mock
    private SsePublisher ssePublisher;

    @Test
    void fetchAsync() {
        var fetchRequestDto = new FetchRequestDto();
        var fetchRequest = new FetchRequest();

        when(fetchRequestMapper.toFetchRequest(fetchRequestDto))
                .thenReturn(fetchRequest);
        when(fetchRequestHandlerFactory.getHandler(fetchRequest))
                .thenReturn(fetchRequestHandler);

        fetchService.fetchAsync(fetchRequestDto, ssePublisher);

        verify(fetchRequestHandler).fetch(fetchRequest, ssePublisher);
        verify(ssePublisher).complete();
    }

    @Test
    void fetchAsync_actionFailed() {
        var fetchRequestDto = new FetchRequestDto();
        var fetchRequest = FetchRequest.builder().fetchType(FetchType.CHANNELS).build();
        var exception = new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "");

        when(fetchRequestMapper.toFetchRequest(fetchRequestDto))
                .thenReturn(fetchRequest);
        when(fetchRequestHandlerFactory.getHandler(fetchRequest))
                .thenReturn(fetchRequestHandler);
        doThrow(exception)
                .when(fetchRequestHandler).fetch(fetchRequest, ssePublisher);
        when(ssePublisher.isOpen())
                .thenReturn(true);

        fetchService.fetchAsync(fetchRequestDto, ssePublisher);

        verify(ssePublisher).publish(any(FetchStatusEvent.class));
        verify(ssePublisher).completeWithError(exception);
    }

    @Test
    void fetchAsync_publisherClosed() {
        var fetchRequestDto = new FetchRequestDto();
        var fetchRequest = FetchRequest.builder().fetchType(FetchType.CHANNELS).build();
        var exception = new PublisherException("");

        when(fetchRequestMapper.toFetchRequest(fetchRequestDto))
                .thenReturn(fetchRequest);
        when(fetchRequestHandlerFactory.getHandler(fetchRequest))
                .thenReturn(fetchRequestHandler);
        doThrow(exception)
                .when(fetchRequestHandler).fetch(fetchRequest, ssePublisher);
        when(ssePublisher.isOpen())
                .thenReturn(false);

        fetchService.fetchAsync(fetchRequestDto, ssePublisher);

        verifyNoMoreInteractions(ssePublisher);
    }
}
