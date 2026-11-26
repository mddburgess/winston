package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.database.entity.fetch.FetchOperationEntity;
import ca.metricalsky.winston.database.entity.fetch.FetchRequestEntity;
import ca.metricalsky.winston.database.repository.fetch.FetchRequestRepository;
import ca.metricalsky.winston.event.EventPublisherService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FetchRequestService {

    private final EventPublisherService eventPublisherService;
    private final FetchRequestRepository fetchRequestRepository;

    @Transactional
    public List<FetchOperationEntity> startProcessingRequest(Long fetchRequestId) {
        var fetchRequest = fetchRequestRepository.findById(fetchRequestId).orElseThrow();
        fetchRequest.setStatus(FetchRequestEntity.Status.FETCHING);
        fetchRequest = fetchRequestRepository.save(fetchRequest);
        eventPublisherService.publishEvent(fetchRequest, null);
        return fetchRequest.getOperations();
    }

    @Transactional
    public void finishProcessingRequest(Long fetchRequestId, Throwable throwable) {
        var fetchRequest = fetchRequestRepository.findById(fetchRequestId).orElseThrow();
        fetchRequest.setStatus(FetchRequestEntity.Status.COMPLETED);
        fetchRequest = fetchRequestRepository.save(fetchRequest);
        eventPublisherService.publishEvent(fetchRequest, throwable);
    }
}
