package ca.metricalsky.winston.dao;

import ca.metricalsky.winston.api.model.PullRequest;
import ca.metricalsky.winston.database.entity.fetch.FetchRequestEntity;
import ca.metricalsky.winston.database.repository.fetch.FetchRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PullRequestDataService {

    private final ConversionService conversionService;
    private final FetchRequestRepository fetchRequestRepository;

    public Long savePullRequest(PullRequest pullRequest) {
        var fetchRequestEntity = conversionService.convert(pullRequest, FetchRequestEntity.class);
        fetchRequestEntity = fetchRequestRepository.save(fetchRequestEntity);
        return fetchRequestEntity.getId();
    }
}
