package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.entity.fetch.FetchOperationEntity;
import ca.metricalsky.winston.entity.fetch.FetchRequestEntity;
import ca.metricalsky.winston.repository.fetch.FetchRequestRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FetchRequestService {

    private final FetchRequestRepository fetchRequestRepository;

    @Transactional
    public List<FetchOperationEntity> startProcessingRequest(Long fetchRequestId) {
        var fetchRequest = fetchRequestRepository.findById(fetchRequestId).orElseThrow();
        fetchRequest.setStatus(FetchRequestEntity.Status.FETCHING);
        fetchRequestRepository.save(fetchRequest);
        return fetchRequest.getOperations();
    }

    @Transactional
    public void finishProcessingRequest(Long fetchRequestId) {
        var fetchRequest = fetchRequestRepository.findById(fetchRequestId).orElseThrow();
        fetchRequest.setStatus(FetchRequestEntity.Status.COMPLETED);
        fetchRequestRepository.save(fetchRequest);
    }
}
