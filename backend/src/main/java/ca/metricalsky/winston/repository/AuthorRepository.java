package ca.metricalsky.winston.repository;

import ca.metricalsky.winston.entity.Author;
import ca.metricalsky.winston.entity.view.AuthorDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, String> {

    Optional<Author> findByChannelUrl(String channelUrl);

    Optional<Author> findByDisplayName(String displayName);

    @Query("""
            SELECT
                a AS author,
                COUNT(DISTINCT c.videoId) AS commentedVideos,
                COUNT(c.id) - COUNT(c.parentId) AS totalComments,
                COUNT(c.parentId) AS totalReplies
            FROM Author a
                JOIN Comment c ON a.id = c.author.id
            GROUP BY a.id
            """)
    List<AuthorDetails> findAllAuthorDetails();
}
