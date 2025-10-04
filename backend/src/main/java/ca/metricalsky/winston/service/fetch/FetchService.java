package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.config.properties.youtube.YouTubeConfig;
import ca.metricalsky.winston.entity.fetch.FetchOperationEntity;
import ca.metricalsky.winston.events.SsePublisher;
import ca.metricalsky.winston.events.SsePublisherHolder;
import ca.metricalsky.winston.repository.fetch.YouTubeRequestRepository;
import ca.metricalsky.winston.service.fetch.operation.FetchOperationHandlerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class FetchService {

    private final FetchOperationHandlerFactory fetchOperationHandlerFactory;
    private final FetchRequestService fetchRequestService;
    private final SsePublisherHolder ssePublisherHolder;
    private final YouTubeConfig youTubeConfig;
    private final YouTubeRequestRepository youTubeRequestRepository;

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
        var startOfToday = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toOffsetDateTime();
        var dailyRequestQuota = youTubeConfig.getDailyRequestQuota();
        var consumedRequestQuota = youTubeRequestRepository.countAllByRequestedAtAfter(startOfToday);

        return dailyRequestQuota - consumedRequestQuota;
    }
}
