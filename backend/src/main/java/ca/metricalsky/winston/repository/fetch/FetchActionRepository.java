package ca.metricalsky.winston.repository.fetch;

import ca.metricalsky.winston.entity.fetch.FetchActionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FetchActionRepository extends JpaRepository<FetchActionEntity, Long> {

}
