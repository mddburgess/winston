package ca.metricalsky.winston.repository;

import ca.metricalsky.winston.entity.VideoEntity;
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
            SELECT DISTINCT v
            FROM VideoEntity v JOIN CommentEntity c ON v.id = c.videoId
            WHERE c.author.displayName = :authorDisplayName
            """)
    List<VideoEntity> findAllByCommentAuthorDisplayName(String authorDisplayName);

    @Query("""
            SELECT v FROM VideoEntity v
            WHERE v.channelId = (SELECT id FROM ChannelEntity WHERE customUrl = :channelHandle)
            ORDER BY v.publishedAt DESC
            """)
    @EntityGraph(attributePaths = "comments")
    List<VideoEntity> findAllByChannelHandle(String channelHandle);

    @Query("SELECT MAX(v.publishedAt) FROM VideoEntity v WHERE v.channelId = :channelId")
    Optional<OffsetDateTime> findLastPublishedAtForChannelId(String channelId);
}
