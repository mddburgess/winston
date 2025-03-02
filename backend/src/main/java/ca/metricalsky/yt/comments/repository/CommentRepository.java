package ca.metricalsky.yt.comments.repository;

import ca.metricalsky.yt.comments.entity.Comment;
import jakarta.persistence.Tuple;
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
            SELECT v.id, COUNT(c.id), SUM(c.totalReplyCount)
            FROM Video v
                JOIN Comment c ON v.id = c.videoId
            GROUP BY v.id
            """)
    List<Tuple> countCommentsForChannelIdGroupByVideoId(String channelId);

    @Query("""
            SELECT v.id, COUNT(r.id)
            FROM Video v
                JOIN Comment c ON v.id = c.videoId
                JOIN Comment r ON c.id = r.parentId
            GROUP BY v.id
            """)
    List<Tuple> countRepliesForChannelIdGroupByVideoId(String channelId);
}
