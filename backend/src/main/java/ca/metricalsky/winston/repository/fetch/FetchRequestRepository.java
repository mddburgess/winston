package ca.metricalsky.winston.repository.fetch;

import ca.metricalsky.winston.entity.fetch.FetchRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FetchRequestRepository extends JpaRepository<FetchRequest, Long> {

}
