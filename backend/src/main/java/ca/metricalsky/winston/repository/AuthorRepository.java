package ca.metricalsky.winston.repository;

import ca.metricalsky.winston.entity.AuthorEntity;
import ca.metricalsky.winston.entity.view.VideoStatisticsView;
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
                v.channelId AS channelId,
                v.id AS videoId,
                COUNT(c.id) - COUNT(c.parentId) AS commentCount,
                COUNT(c.parentId) AS replyCount,
                MAX(c.publishedAt) AS lastCommentedAt
            FROM CommentEntity c
                LEFT JOIN VideoEntity v ON c.videoId = v.id
            WHERE c.author.id = :id
            GROUP BY v.id
            """)
    List<VideoStatisticsView> findVideoStatisticsByAuthorId(String id);
}
