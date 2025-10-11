package ca.metricalsky.winston.repository;

import ca.metricalsky.winston.entity.VideoCommentsEntity;
import ca.metricalsky.winston.entity.view.CommentStatisticsView;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoCommentsRepository extends ListCrudRepository<VideoCommentsEntity, String> {

    @Query("""
            SELECT
                COUNT(vc.videoId) AS videoCount,
                COALESCE(SUM(vc.commentCount), 0) AS commentCount,
                COALESCE(SUM(vc.replyCount), 0) AS replyCount
            FROM VideoCommentsEntity vc
            """)
    CommentStatisticsView getCommentStatistics();
}
