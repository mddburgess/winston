package ca.metricalsky.winston.repository;

import ca.metricalsky.winston.entity.Video;
import ca.metricalsky.winston.entity.view.VideoCount;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<Video, String> {

    @Query("""
            SELECT c.id AS channelId, COUNT(v.id) AS videos
            FROM Channel c
                JOIN Video v ON c.id = v.channelId
            GROUP BY c.id
            """)
    List<VideoCount> countAllByChannelId();

    @Query("""
            SELECT DISTINCT v
            FROM Video v JOIN Comment c ON v.id = c.videoId
            WHERE c.author.displayName = :authorDisplayName
            """)
    List<Video> findAllByCommentAuthorDisplayName(String authorDisplayName);

    @Query("""
            SELECT v FROM Video v
            WHERE v.channelId = (SELECT id FROM Channel WHERE customUrl = :channelHandle)
            ORDER BY v.publishedAt DESC
            """)
    @EntityGraph(attributePaths = "comments")
    List<Video> findAllByChannelHandle(String channelHandle);

    @Query("SELECT MAX(v.publishedAt) FROM Video v WHERE v.channelId = :channelId")
    Optional<OffsetDateTime> findLastPublishedAtForChannelId(String channelId);
}
