package ca.metricalsky.yt.comments.repository;

import ca.metricalsky.yt.comments.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, String> {

}
