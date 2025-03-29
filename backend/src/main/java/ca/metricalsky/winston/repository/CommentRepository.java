package ca.metricalsky.winston.repository;

import ca.metricalsky.winston.entity.Comment;
import ca.metricalsky.winston.entity.view.CommentCount;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {

    @Query("""
            SELECT c FROM Comment c
            WHERE c.videoId = :videoId AND c.parentId IS NULL
            ORDER BY c.publishedAt ASC
            """)
    @EntityGraph(attributePaths = {"author", "replies", "replies.author"})
    List<Comment> findTopLevelCommentsByVideoId(String videoId);

    @Query("""
            SELECT c FROM Comment c
            WHERE c.parentId IS NULL
            AND (c.author.id = :authorId OR c.id IN (
                SELECT r.parentId FROM Comment r
                WHERE r.parentId IS NOT NULL AND r.author.id = :authorId
            ))
            """)
    @EntityGraph(attributePaths = {"author", "replies", "replies.author"})
    List<Comment> findAllWithContextByAuthorId(String authorId);

    @Query("""
            SELECT
                v.id AS videoId,
                COUNT(c.id) AS commentsAndReplies,
                COUNT(c.parentId) AS replies,
                COALESCE(SUM(c.totalReplyCount), 0) AS totalReplies
            FROM Video v
                JOIN Comment c ON v.id = c.videoId
            WHERE v.channelId = :channelId
            GROUP BY v.id
            """)
    List<CommentCount> countCommentsForChannelIdGroupByVideoId(String channelId);

    @Query("""
            SELECT
                COUNT(c.id) AS commentsAndReplies,
                COUNT(c.parentId) AS replies,
                COALESCE(SUM(c.totalReplyCount), 0) AS totalReplies
            FROM Comment c
            WHERE c.videoId = :videoId
            """)
    CommentCount countCommentsForVideoId(String videoId);
}
