package ca.metricalsky.yt.comments.repository.fetch;

import ca.metricalsky.yt.comments.entity.fetch.FetchRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FetchRequestRepository extends JpaRepository<FetchRequest, UUID> {

}
