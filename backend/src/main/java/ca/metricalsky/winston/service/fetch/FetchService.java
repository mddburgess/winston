package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.api.model.FetchRequest;
import ca.metricalsky.winston.events.FetchStatusEvent;
import ca.metricalsky.winston.events.SsePublisher;
import ca.metricalsky.winston.mapper.entity.FetchRequestMapper;
import ca.metricalsky.winston.repository.fetch.FetchRequestRepository;
import ca.metricalsky.winston.repository.fetch.YouTubeRequestRepository;
import ca.metricalsky.winston.service.fetch.operation.FetchOperationHandlerFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class FetchService {

    private final FetchOperationHandlerFactory fetchOperationHandlerFactory;
    private final FetchRequestMapper fetchRequestMapper;
    private final FetchRequestRepository fetchRequestRepository;
    private final FetchRequestService fetchRequestService;
    private final YouTubeRequestRepository youTubeRequestRepository;

    @Getter
    @Value("${youtube.quota.daily}")
    private int dailyQuota;

    public Long save(FetchRequest fetchRequest) {
        var fetchRequestEntity = fetchRequestMapper.toFetchRequestEntity(fetchRequest);
        fetchRequestEntity = fetchRequestRepository.save(fetchRequestEntity);
        return fetchRequestEntity.getId();
    }

    @Async
    public void fetchAsync(Long fetchRequestId, SsePublisher ssePublisher) {
        try {
            var fetchOperations = fetchRequestService.startProcessingRequest(fetchRequestId);

            fetchOperations.forEach(fetchOperation ->
                    fetchOperationHandlerFactory.getHandler(fetchOperation).fetch(fetchOperation, ssePublisher));

            fetchRequestService.finishProcessingRequest(fetchRequestId);
            ssePublisher.publish(FetchStatusEvent.completed());
            ssePublisher.complete();
        } catch (RuntimeException ex) {
            if (ssePublisher.isOpen()) {
                ssePublisher.publish(FetchStatusEvent.failed(ex));
                ssePublisher.completeWithError(ex);
            }
        }
    }

    public int getAvailableQuota() {
        var startOfToday = LocalDate.now().atStartOfDay().atOffset(ZoneOffset.UTC);
        return dailyQuota - youTubeRequestRepository.countAllByRequestedAtAfter(startOfToday);
    }
}
