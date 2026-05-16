package ca.metricalsky.winston.database.repository.comment

import ca.metricalsky.winston.database.entity.comment.CommentEntity
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime

@Repository
@Transactional
class CommentJdbcRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {
    fun saveAll(comments: List<CommentEntity>): List<CommentEntity> {
        if (comments.isEmpty()) {
            return comments
        }

        val replies = comments.mapNotNull { it.replies }.flatten().filterNotNull()
        val commentsAndReplies = comments + replies

        val lastFetchedAt = OffsetDateTime.now()
        commentsAndReplies.forEach { it.lastFetchedAt = lastFetchedAt }

        val commentIds = commentsAndReplies.mapNotNull { it.id }
        val idsToUpdate = selectCommentIds(commentIds)

        val partition = commentsAndReplies.partition { it.id in idsToUpdate }
        val commentsToUpdate = partition.first
        val commentsToInsert = partition.second

        insertComments(commentsToInsert)
        updateComments(commentsToUpdate)

        return comments
    }

    private fun selectCommentIds(commentIds: Collection<String>): Set<String> {
        return HashSet<String>(
            jdbcTemplate.queryForList(
                "SELECT id FROM comments WHERE id IN (:ids)",
                MapSqlParameterSource("ids", commentIds),
                String::class.java
            )
        )
    }

    private fun insertComments(
        comments: Collection<CommentEntity>
    ) {
        jdbcTemplate.batchUpdate(
            """
            INSERT INTO comments (id, video_id, parent_id, author_id, text_display, text_original,
                                  total_reply_count, published_at, updated_at, last_fetched_at, like_count)
            VALUES (:id, :videoId, :parentId, :author.id, :textDisplay, :textOriginal,
                    :totalReplyCount, :publishedAt, :updatedAt, :lastFetchedAt, :likeCount)
            """,
            SqlParameterSourceUtils.createBatch(comments)
        )
    }

    private fun updateComments(
        comments: Collection<CommentEntity>
    ) {
        jdbcTemplate.batchUpdate("""
            UPDATE comments
            SET video_id = :videoId,
                parent_id = :parentId,
                author_id = :author.id,
                text_display = :textDisplay,
                text_original = :textOriginal,
                total_reply_count = :totalReplyCount,
                published_at = :publishedAt,
                updated_at = :updatedAt,
                last_fetched_at = :lastFetchedAt,
                like_count = :likeCount
            WHERE id = :id
            """,
            SqlParameterSourceUtils.createBatch(comments)
        )
    }
}
