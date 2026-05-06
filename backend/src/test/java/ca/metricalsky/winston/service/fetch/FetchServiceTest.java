package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.config.properties.youtube.YouTubeConfig;
import ca.metricalsky.winston.database.entity.fetch.FetchRequestEntity;
import ca.metricalsky.winston.database.repository.fetch.YouTubeRequestRepository;
import ca.metricalsky.winston.events.PublisherException;
import ca.metricalsky.winston.events.SsePublisher;
import ca.metricalsky.winston.events.SsePublisherHolder;
import ca.metricalsky.winston.exception.AppException;
import ca.metricalsky.winston.service.fetch.operation.FetchOperationHandler;
import ca.metricalsky.winston.service.fetch.operation.FetchOperationHandlerFactory;
import ca.metricalsky.winston.test.faker.WinstonFaker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
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
    private FetchRequestService fetchRequestService;
    @Mock
    private SsePublisher ssePublisher;
    @Mock
    private SsePublisherHolder ssePublisherHolder;
    @Mock
    private YouTubeConfig youTubeConfig;
    @Mock
    private YouTubeRequestRepository youTubeRequestRepository;

    @Test
    void fetchAsync() {
        var fetchRequest = buildFetchRequestEntity();

        when(fetchRequestService.startProcessingRequest(fetchRequest.getId()))
                .thenReturn(fetchRequest.getOperations());
        when(fetchOperationHandlerFactory.getHandler(fetchRequest.getOperations().getFirst()))
                .thenReturn(fetchOperationHandler);

        fetchService.fetchAsync(fetchRequest.getId(), ssePublisher);

        verify(ssePublisherHolder).hold(ssePublisher);
        verify(fetchOperationHandler).fetch(fetchRequest.getOperations().getFirst());
        verify(fetchRequestService).finishProcessingRequest(fetchRequest.getId());
        verify(ssePublisher).complete();
        verify(ssePublisherHolder).clear();
    }

    @Test
    void fetchAsync_actionFailed() {
        var fetchRequest = buildFetchRequestEntity();
        var exception = new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "");

        when(fetchRequestService.startProcessingRequest(fetchRequest.getId()))
                .thenReturn(fetchRequest.getOperations());
        when(fetchOperationHandlerFactory.getHandler(fetchRequest.getOperations().getFirst()))
                .thenReturn(fetchOperationHandler);
        doThrow(exception)
                .when(fetchOperationHandler).fetch(fetchRequest.getOperations().getFirst());

        fetchService.fetchAsync(fetchRequest.getId(), ssePublisher);

        verify(ssePublisherHolder).hold(ssePublisher);
        verify(ssePublisher).completeWithError(exception);
        verify(ssePublisherHolder).clear();
    }

    @Test
    void fetchAsync_publisherClosed() {
        var fetchRequest = buildFetchRequestEntity();
        var exception = new PublisherException("");

        when(fetchRequestService.startProcessingRequest(fetchRequest.getId()))
                .thenReturn(fetchRequest.getOperations());
        when(fetchOperationHandlerFactory.getHandler(fetchRequest.getOperations().getFirst()))
                .thenReturn(fetchOperationHandler);
        doThrow(exception)
                .when(fetchOperationHandler).fetch(fetchRequest.getOperations().getFirst());

        fetchService.fetchAsync(fetchRequest.getId(), ssePublisher);

        verify(ssePublisherHolder).hold(ssePublisher);
        verify(ssePublisherHolder).clear();
    }

    @Test
    void getAvailableQuota() {
        when(youTubeConfig.getDailyRequestQuota())
                .thenReturn(10000);
        when(youTubeRequestRepository.countAllByRequestedAtAfter(any(OffsetDateTime.class)))
                .thenReturn(1);

        var availableQuota = fetchService.getAvailableQuota();

        assertThat(availableQuota)
                .isEqualTo(9999);
    }

    private static FetchRequestEntity buildFetchRequestEntity() {
        var faker = new WinstonFaker();
        var fetchRequest = faker.database().fetchRequest().minimalEntity();
        var fetchOperation = faker.database().fetchOperation().channels(fetchRequest);
        fetchRequest.setOperations(List.of(fetchOperation));
        return fetchRequest;
    }
}
