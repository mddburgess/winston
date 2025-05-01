package ca.metricalsky.winston.repository;

import ca.metricalsky.winston.entity.Channel;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, String> {

    @NonNull
    @EntityGraph(attributePaths = {"topics", "keywords"})
    List<Channel> findAll();

    @EntityGraph(attributePaths = {"topics", "keywords"})
    Optional<Channel> findByCustomUrl(String customUrl);
}
