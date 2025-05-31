package ca.metricalsky.winston.repository;

import ca.metricalsky.winston.entity.CommentEntity;
import ca.metricalsky.winston.entity.view.CommentCountView;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, String> {

    @Query("""
            SELECT c FROM CommentEntity c
            WHERE c.videoId = :videoId
            AND c.parentId IS NULL
            ORDER BY c.publishedAt ASC
            """)
    @EntityGraph(attributePaths = {"author", "replies", "replies.author"})
    List<CommentEntity> findCommentsForVideo(String videoId);

    @Query("""
            SELECT c FROM CommentEntity c
            WHERE c.videoId = :videoId
            AND c.parentId IS NULL
            AND (c.author.displayName = :authorDisplayName OR c.id IN (
                SELECT r.parentId FROM CommentEntity r
                WHERE r.parentId IS NOT NULL
                AND r.videoId = :videoId
                AND r.author.displayName = :authorDisplayName
            ))
            """)
    @EntityGraph(attributePaths = {"author", "replies", "replies.author"})
    List<CommentEntity> findCommentsForVideoByAuthor(String videoId, String authorDisplayName);

    @Query("""
            SELECT c FROM CommentEntity c
            WHERE c.parentId IS NULL
            AND (c.author.id = :authorId OR c.id IN (
                SELECT r.parentId FROM CommentEntity r
                WHERE r.parentId IS NOT NULL AND r.author.id = :authorId
            ))
            """)
    @EntityGraph(attributePaths = {"author", "replies", "replies.author"})
    @Deprecated(since = "1.3.0", forRemoval = true)
    List<CommentEntity> findAllWithContextByAuthorId(String authorId);

    @Query("""
            SELECT c.id
            FROM CommentEntity c
                LEFT JOIN CommentEntity r ON c.id = r.parentId
            WHERE c.videoId = :videoId
            GROUP BY c.id
            HAVING c.totalReplyCount > COUNT(r.id)
            """)
    List<String> findIdsMissingRepliesByVideoId(String videoId);

    @Query("""
            SELECT
                v.id AS videoId,
                COUNT(c.id) AS commentsAndReplies,
                COUNT(c.parentId) AS replies,
                COALESCE(SUM(c.totalReplyCount), 0) AS totalReplies
            FROM ChannelEntity ch
                JOIN VideoEntity v ON ch.id = v.channelId
                JOIN CommentEntity c ON v.id = c.videoId
            WHERE ch.customUrl = :channelCustomUrl
            GROUP BY v.id
            """)
    List<CommentCountView> countCommentsByChannelCustomUrl(String channelCustomUrl);

    @Query("""
            SELECT
                COUNT(c.id) AS commentsAndReplies,
                COUNT(c.parentId) AS replies,
                COALESCE(SUM(c.totalReplyCount), 0) AS totalReplies
            FROM CommentEntity c
            WHERE c.videoId = :videoId
            """)
    CommentCountView countCommentsForVideoId(String videoId);
}
