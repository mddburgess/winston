package ca.metricalsky.winston.repository;

import ca.metricalsky.winston.entity.VideoEntity;
import ca.metricalsky.winston.entity.view.ChannelVideoView;
import ca.metricalsky.winston.entity.view.VideoCountView;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<VideoEntity, String> {

    @Query("""
            SELECT c.id AS channelId, COUNT(v.id) AS videos
            FROM ChannelEntity c
                JOIN VideoEntity v ON c.id = v.channelId
            GROUP BY c.id
            """)
    List<VideoCountView> countAllByChannelId();

    @Query("""
            SELECT v FROM VideoEntity v
            WHERE v.channelId = (SELECT id FROM ChannelEntity WHERE customUrl = :channelHandle)
            ORDER BY v.publishedAt DESC
            """)
    @EntityGraph(attributePaths = "comments")
    List<VideoEntity> findAllByChannelHandle(String channelHandle);

    @Query("SELECT MAX(v.publishedAt) FROM VideoEntity v WHERE v.channelId = :channelId")
    Optional<OffsetDateTime> findLastPublishedAtForChannelId(String channelId);

    @Query("""
            SELECT c AS channel, v AS video
            FROM ChannelEntity c JOIN VideoEntity v ON c.id = v.channelId
            WHERE v.id = :videoId
            """)
    Optional<ChannelVideoView> findChannelVideoById(String videoId);

    @Query("""
            SELECT DISTINCT ch AS channel, v AS video
            FROM ChannelEntity ch
                JOIN VideoEntity v ON ch.id = v.channelId
                JOIN CommentEntity co ON v.id = co.videoId
            WHERE co.author.displayName = :authorDisplayName
            """)
    @EntityGraph(attributePaths = "comments")
    List<ChannelVideoView> findAllChannelVideosByAuthorDisplayName(String authorDisplayName);
}
