package ca.metricalsky.yt.comments.repository;

import ca.metricalsky.yt.comments.entity.Video;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, String> {

    @Query("SELECT c.id, COUNT(v.id) FROM Channel c LEFT JOIN Video v ON c.id = v.channelId GROUP BY c.id")
    List<Tuple> countAllByChannelId();

    List<Video> findAllByChannelId(String channelId);
}
