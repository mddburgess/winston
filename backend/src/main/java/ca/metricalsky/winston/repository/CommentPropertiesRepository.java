package ca.metricalsky.winston.repository;

import ca.metricalsky.winston.entity.CommentPropertiesEntity;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentPropertiesRepository extends ListCrudRepository<CommentPropertiesEntity, String> {

}
