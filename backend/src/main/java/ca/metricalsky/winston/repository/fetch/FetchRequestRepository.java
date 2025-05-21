package ca.metricalsky.winston.repository.fetch;

import ca.metricalsky.winston.entity.fetch.FetchRequestEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FetchRequestRepository extends JpaRepository<FetchRequestEntity, Long> {

    @Override
    @NonNull
    @EntityGraph(attributePaths = "operations")
    Optional<FetchRequestEntity> findById(@NonNull Long id);
}
