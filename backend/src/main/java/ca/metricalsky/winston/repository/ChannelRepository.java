package ca.metricalsky.winston.repository;

import ca.metricalsky.winston.entity.ChannelEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChannelRepository extends JpaRepository<ChannelEntity, String> {

    @NonNull
    @EntityGraph(attributePaths = {"topics", "keywords"})
    List<ChannelEntity> findAll();

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"topics", "keywords"})
    Optional<ChannelEntity> findById(@NonNull String id);

    @EntityGraph(attributePaths = {"topics", "keywords"})
    Optional<ChannelEntity> findByCustomUrl(String customUrl);
}
