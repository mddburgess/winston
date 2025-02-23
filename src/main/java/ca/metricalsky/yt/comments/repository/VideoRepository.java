package ca.metricalsky.yt.comments.repository;

import ca.metricalsky.yt.comments.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends JpaRepository<Video, String> {

}
