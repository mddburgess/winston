package ca.metricalsky.winston.repository;

import ca.metricalsky.winston.entity.ChannelEntity;
import ca.metricalsky.winston.database.view.ChannelVideoStatisticsView;
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

    @Query("""
            SELECT
                c.customUrl AS channelHandle,
                c.id AS channelId,
                c.publishedAt AS channelPublishedAt,
                c.videoCount AS channelVideoCount,
                COUNT(v.id) AS videoCount,
                MAX(v.publishedAt) AS latestVideoPublishedAt
            FROM ChannelEntity c
            LEFT JOIN VideoEntity v ON c.id = v.channelId
            WHERE c.customUrl = :customUrl
            GROUP BY c.id
            """)
    Optional<ChannelVideoStatisticsView> findChannelVideoStatisticsByCustomUrl(String customUrl);
}
