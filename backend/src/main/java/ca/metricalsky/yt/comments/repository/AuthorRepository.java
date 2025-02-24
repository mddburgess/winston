package ca.metricalsky.yt.comments.repository;

import ca.metricalsky.yt.comments.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, String> {

}
