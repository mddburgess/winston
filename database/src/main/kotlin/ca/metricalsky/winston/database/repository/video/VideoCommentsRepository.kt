package ca.metricalsky.winston.database.repository.video

import ca.metricalsky.winston.database.entity.video.VideoCommentsEntity
import ca.metricalsky.winston.database.view.CommentStatisticsView
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface VideoCommentsRepository: JpaRepository<VideoCommentsEntity, String> {

    @Query("""
        SELECT
            COUNT(vc.videoId) AS videoCount,
            COALESCE(SUM(vc.commentCount), 0) AS commentCount,
            COALESCE(SUM(vc.replyCount), 0) AS replyCount
        FROM VideoCommentsEntity vc
    """)
    fun getCommentStatistics(): CommentStatisticsView
}
