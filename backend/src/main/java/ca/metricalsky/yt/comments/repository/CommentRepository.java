package ca.metricalsky.yt.comments.repository;

import ca.metricalsky.yt.comments.entity.Comment;
import ca.metricalsky.yt.comments.entity.view.CommentCount;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {

    @EntityGraph(attributePaths = {"author", "replies"})
    List<Comment> findAllByVideoIdOrderByPublishedAtAsc(String videoId);

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
            SELECT v.id AS videoId, COUNT(c.id) AS comments, SUM(c.totalReplyCount) AS replies
            FROM Video v
                JOIN Comment c ON v.id = c.videoId
            WHERE v.channelId = :channelId
            GROUP BY v.id
            """)
    List<CommentCount> countCommentsForChannelIdGroupByVideoId(String channelId);

    @Query("""
            SELECT v.id AS videoId, COUNT(r.id) AS comments
            FROM Video v
                JOIN Comment c ON v.id = c.videoId
                JOIN Comment r ON c.id = r.parentId
            WHERE v.channelId = :channelId
            GROUP BY v.id
            """)
    List<CommentCount> countRepliesForChannelIdGroupByVideoId(String channelId);
}
