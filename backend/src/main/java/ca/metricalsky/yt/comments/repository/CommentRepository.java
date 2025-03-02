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
    List<Comment> findAllByVideoId(String videoId);

    @Query("""
            SELECT v.id AS videoId, COUNT(c.id) AS comments, SUM(c.totalReplyCount) AS replies
            FROM Video v
                JOIN Comment c ON v.id = c.videoId
            GROUP BY v.id
            """)
    List<CommentCount> countCommentsForChannelIdGroupByVideoId(String channelId);

    @Query("""
            SELECT v.id AS videoId, COUNT(r.id) AS comments
            FROM Video v
                JOIN Comment c ON v.id = c.videoId
                JOIN Comment r ON c.id = r.parentId
            GROUP BY v.id
            """)
    List<CommentCount> countRepliesForChannelIdGroupByVideoId(String channelId);
}
