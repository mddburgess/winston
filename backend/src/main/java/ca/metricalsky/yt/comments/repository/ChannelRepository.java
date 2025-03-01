package ca.metricalsky.yt.comments.repository;

import ca.metricalsky.yt.comments.entity.Channel;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, String> {

    @NonNull
    @EntityGraph(attributePaths = {"topics", "keywords"})
    List<Channel> findAll();
}
