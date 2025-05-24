package ca.metricalsky.winston.repository.fetch;

import ca.metricalsky.winston.entity.fetch.YouTubeRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;

@Repository
public interface YouTubeRequestRepository extends JpaRepository<YouTubeRequestEntity, Long> {

    int countAllByRequestedAtAfter(OffsetDateTime date);
}
