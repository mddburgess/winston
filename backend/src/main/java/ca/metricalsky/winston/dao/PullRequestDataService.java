package ca.metricalsky.winston.dao;

import ca.metricalsky.winston.api.model.PullRequest;
import ca.metricalsky.winston.mapper.entity.FetchRequestEntityMapper;
import ca.metricalsky.winston.repository.fetch.FetchRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PullRequestDataService {

    private final FetchRequestEntityMapper fetchRequestEntityMapper;
    private final FetchRequestRepository fetchRequestRepository;

    public Long savePullRequest(PullRequest pullRequest) {
        var fetchRequestEntity = fetchRequestEntityMapper.toFetchRequestEntity(pullRequest);
        fetchRequestEntity = fetchRequestRepository.save(fetchRequestEntity);
        return fetchRequestEntity.getId();
    }
}
