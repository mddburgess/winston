package ca.metricalsky.winston.repository;

import ca.metricalsky.winston.entity.AuthorEntity;
import ca.metricalsky.winston.entity.view.AuthorDetailsView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorEntity, String> {

    Optional<AuthorEntity> findByChannelUrl(String channelUrl);

    Optional<AuthorEntity> findByDisplayName(String displayName);

    @Query("""
            SELECT
                a AS author,
                COUNT(DISTINCT v.channelId) AS channelCount,
                COUNT(DISTINCT c.videoId) AS videoCount,
                COUNT(c.id) - COUNT(c.parentId) AS commentCount,
                COUNT(c.parentId) AS replyCount
            FROM AuthorEntity a
                LEFT JOIN CommentEntity c ON a.id = c.author.id
                LEFT JOIN VideoEntity v ON c.videoId = v.id
            GROUP BY a.id
            """)
    List<AuthorDetailsView> findAllAuthorDetails();
}
