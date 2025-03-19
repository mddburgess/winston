package ca.metricalsky.winston.repository;

import ca.metricalsky.winston.entity.Video;
import ca.metricalsky.winston.entity.view.VideoCount;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, String> {

    List<Video> findAllByChannelIdOrderByPublishedAtDesc(String channelId);

    List<Video> findByChannelIdOrderByPublishedAtDesc(String channelId, Pageable pageable);


    @Query("""
            SELECT c.id AS channelId, COUNT(v.id) AS videos
            FROM Channel c
                JOIN Video v ON c.id = v.channelId
            GROUP BY c.id
            """)
    List<VideoCount> countAllByChannelId();

    @Query("SELECT MAX(v.publishedAt) FROM Video v WHERE v.channelId = :channelId")
    OffsetDateTime getLastPublishedAtForChannelId(String channelId);
}
