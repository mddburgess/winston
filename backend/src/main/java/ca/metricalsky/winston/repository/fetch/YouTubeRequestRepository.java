package ca.metricalsky.winston.repository.fetch;

import ca.metricalsky.winston.entity.fetch.YouTubeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YouTubeRequestRepository extends JpaRepository<YouTubeRequest, Long> {

}
