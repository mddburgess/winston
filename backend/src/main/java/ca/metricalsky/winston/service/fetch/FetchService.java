package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.entity.fetch.FetchOperationEntity;
import ca.metricalsky.winston.events.SsePublisher;
import ca.metricalsky.winston.events.SsePublisherHolder;
import ca.metricalsky.winston.repository.fetch.YouTubeRequestRepository;
import ca.metricalsky.winston.service.fetch.operation.FetchOperationHandlerFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class FetchService {

    private final FetchOperationHandlerFactory fetchOperationHandlerFactory;
    private final FetchRequestService fetchRequestService;
    private final SsePublisherHolder ssePublisherHolder;
    private final YouTubeRequestRepository youTubeRequestRepository;

    @Getter
    @Value("${youtube.quota.daily}")
    private int dailyQuota;

    @Async
    public void fetchAsync(Long fetchRequestId, SsePublisher ssePublisher) {
        try {
            ssePublisherHolder.hold(ssePublisher);
            var fetchOperations = fetchRequestService.startProcessingRequest(fetchRequestId);

            fetchOperations.stream()
                    .sorted(Comparator.comparing(FetchOperationEntity::getId))
                    .forEach(fetchOperation ->
                            fetchOperationHandlerFactory.getHandler(fetchOperation).fetch(fetchOperation));

            fetchRequestService.finishProcessingRequest(fetchRequestId);
            ssePublisher.complete();
        } catch (RuntimeException ex) {
            ssePublisher.completeWithError(ex);
        } finally {
            ssePublisherHolder.clear();
        }
    }

    public int getAvailableQuota() {
        var startOfToday = LocalDate.now().atStartOfDay().atOffset(ZoneOffset.UTC);
        return dailyQuota - youTubeRequestRepository.countAllByRequestedAtAfter(startOfToday);
    }
}
