package ca.metricalsky.yt.comments.repository;

import ca.metricalsky.yt.comments.entity.Video;
import ca.metricalsky.yt.comments.entity.view.VideoCount;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
}
