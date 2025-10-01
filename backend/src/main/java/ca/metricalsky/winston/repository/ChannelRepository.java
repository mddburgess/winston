package ca.metricalsky.winston.repository;

import ca.metricalsky.winston.entity.ChannelEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChannelRepository extends JpaRepository<ChannelEntity, String> {

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"keywords", "properties", "topics"})
    List<ChannelEntity> findAll();

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"keywords", "properties", "topics"})
    Optional<ChannelEntity> findById(@NonNull String id);

    @EntityGraph(attributePaths = {"keywords", "properties", "topics"})
    Optional<ChannelEntity> findByCustomUrl(String customUrl);

    @Query("""
            SELECT c
            FROM ChannelEntity c
                LEFT JOIN ChannelPropertiesEntity cp ON c.id = cp.channelId
            WHERE cp.archived IS NULL OR cp.archived = false
            """)
    @EntityGraph(attributePaths = {"keywords", "properties", "topics"})
    List<ChannelEntity> findAllUnarchived();

    @Query("SELECT c.id FROM ChannelEntity c WHERE c.customUrl = :customUrl")
    Optional<String> findIdByCustomUrl(String customUrl);
}
