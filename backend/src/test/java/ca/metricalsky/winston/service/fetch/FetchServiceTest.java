package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.api.model.FetchRequest;
import ca.metricalsky.winston.entity.fetch.FetchOperationEntity;
import ca.metricalsky.winston.entity.fetch.FetchOperationEntity.Type;
import ca.metricalsky.winston.entity.fetch.FetchRequestEntity;
import ca.metricalsky.winston.events.FetchStatusEvent;
import ca.metricalsky.winston.events.PublisherException;
import ca.metricalsky.winston.events.SsePublisher;
import ca.metricalsky.winston.exception.AppException;
import ca.metricalsky.winston.mapper.entity.FetchRequestMapper;
import ca.metricalsky.winston.repository.fetch.FetchRequestRepository;
import ca.metricalsky.winston.repository.fetch.YouTubeRequestRepository;
import ca.metricalsky.winston.service.fetch.operation.FetchOperationHandler;
import ca.metricalsky.winston.service.fetch.operation.FetchOperationHandlerFactory;
import ca.metricalsky.winston.test.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
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
    private FetchRequestRepository fetchRequestRepository;
    @Mock
    private FetchRequestService fetchRequestService;
    @Mock
    private SsePublisher ssePublisher;
    @Mock
    private YouTubeRequestRepository youTubeRequestRepository;

    @Test
    void save() {
        var fetchRequest = new FetchRequest();
        var fetchRequestEntity = buildFetchRequestEntity();

        when(fetchRequestMapper.toFetchRequest(fetchRequest))
                .thenReturn(fetchRequestEntity);
        when(fetchRequestRepository.save(fetchRequestEntity))
                .thenAnswer(returnsFirstArg());

        var fetchRequestId = fetchService.save(fetchRequest);

        assertThat(fetchRequestId)
                .isEqualTo(fetchRequestEntity.getId());
    }

    @Test
    void fetchAsync() {
        var fetchRequest = buildFetchRequestEntity();

        when(fetchRequestService.startProcessingRequest(fetchRequest.getId()))
                .thenReturn(fetchRequest.getOperations());
        when(fetchOperationHandlerFactory.getHandler(fetchRequest.getOperations().getFirst()))
                .thenReturn(fetchOperationHandler);

        fetchService.fetchAsync(fetchRequest.getId(), ssePublisher);

        verify(fetchOperationHandler).fetch(fetchRequest.getOperations().getFirst(), ssePublisher);
        verify(fetchRequestService).finishProcessingRequest(fetchRequest.getId());
        verify(ssePublisher).complete();
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
                .when(fetchOperationHandler).fetch(fetchRequest.getOperations().getFirst(), ssePublisher);
        when(ssePublisher.isOpen())
                .thenReturn(true);

        fetchService.fetchAsync(fetchRequest.getId(), ssePublisher);

        verify(ssePublisher).publish(any(FetchStatusEvent.class));
        verify(ssePublisher).completeWithError(exception);
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
                .when(fetchOperationHandler).fetch(fetchRequest.getOperations().getFirst(), ssePublisher);
        when(ssePublisher.isOpen())
                .thenReturn(false);

        fetchService.fetchAsync(fetchRequest.getId(), ssePublisher);

        verifyNoMoreInteractions(ssePublisher);
    }

    @Test
    void getRemainingQuota() {
        ReflectionTestUtils.setField(fetchService, "dailyQuota", 10000);

        when(youTubeRequestRepository.countAllByRequestedAtAfter(any(OffsetDateTime.class)))
                .thenReturn(1);

        var remainingQuota = fetchService.getRemainingQuota();

        assertThat(remainingQuota)
                .isEqualTo(9999);
    }

    private static FetchRequestEntity buildFetchRequestEntity() {
        var fetchOperationEntity = FetchOperationEntity.builder()
                .operationType(Type.CHANNELS)
                .build();
        return FetchRequestEntity.builder()
                .id(TestUtils.randomLong())
                .operations(List.of(fetchOperationEntity))
                .build();
    }
}
