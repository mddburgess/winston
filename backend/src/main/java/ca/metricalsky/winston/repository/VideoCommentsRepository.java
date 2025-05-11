package ca.metricalsky.winston.repository;

import ca.metricalsky.winston.entity.VideoCommentsEntity;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoCommentsRepository extends ListCrudRepository<VideoCommentsEntity, String> {

}
