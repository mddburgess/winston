package ca.metricalsky.yt.comments.repository.fetch;

import ca.metricalsky.yt.comments.entity.fetch.FetchOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FetchOperationRepository extends JpaRepository<FetchOperation, Long> {

}
