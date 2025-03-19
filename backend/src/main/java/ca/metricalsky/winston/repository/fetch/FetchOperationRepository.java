package ca.metricalsky.winston.repository.fetch;

import ca.metricalsky.winston.entity.fetch.FetchOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FetchOperationRepository extends JpaRepository<FetchOperation, Long> {

}
