package ca.metricalsky.winston.database.repository.comment

import ca.metricalsky.winston.database.entity.comment.CommentEntity
import ca.metricalsky.winston.database.view.CommentCountView
import ca.metricalsky.winston.database.view.ReplyStatisticsView
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CommentRepository: JpaRepository<CommentEntity, String> {

    @Query("""
        SELECT c FROM CommentEntity c
        WHERE c.videoId = :videoId AND c.parentId IS NULL
        ORDER BY c.publishedAt ASC
    """)
    @EntityGraph(attributePaths = ["author", "properties", "replies", "replies.properties", "replies.author"])
    fun findCommentsForVideo(videoId: String): MutableList<CommentEntity>

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
    @EntityGraph(attributePaths = ["author", "properties", "replies", "replies.properties", "replies.author"])
    fun findCommentsForVideoByAuthor(videoId: String, authorDisplayName: String): MutableList<CommentEntity>

    @Query("""
        SELECT c.id
        FROM CommentEntity c
            LEFT JOIN CommentEntity r ON c.id = r.parentId
        WHERE c.videoId = :videoId
            AND c.parentId IS NULL
        GROUP BY c.id
        HAVING c.totalReplyCount > COUNT(r.id)
    """)
    fun findIdsMissingRepliesByVideoId(videoId: String): MutableList<String>

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
    fun countCommentsByChannelCustomUrl(channelCustomUrl: String): MutableList<CommentCountView>

    @Query("""
        SELECT
            COUNT(c.id) AS commentsAndReplies,
            COUNT(c.parentId) AS replies,
            COALESCE(SUM(c.totalReplyCount), 0) AS totalReplies
        FROM CommentEntity c
        WHERE c.videoId = :videoId
    """)
    fun countCommentsForVideoId(videoId: String): CommentCountView

    @Query("""
        SELECT
            c.id AS commentId,
            c.publishedAt AS commentPublishedAt,
            c.totalReplyCount AS commentReplyCount,
            c.lastFetchedAt AS commentLastFetchedAt,
            COUNT(r.id) AS fetchedReplyCount,
            MAX(r.publishedAt) AS mostRecentReplyPublishedAt
        FROM CommentEntity c
        LEFT JOIN CommentEntity r ON c.id = r.parentId
        WHERE c.id = :commentId
        GROUP BY c.id
    """)
    fun getReplyStatisticsByCommentId(commentId: String): Optional<ReplyStatisticsView>
}
