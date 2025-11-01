package ca.metricalsky.winston.repository;

import ca.metricalsky.winston.entity.ChannelPropertiesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelPropertiesRepository extends JpaRepository<ChannelPropertiesEntity, String> {

}
