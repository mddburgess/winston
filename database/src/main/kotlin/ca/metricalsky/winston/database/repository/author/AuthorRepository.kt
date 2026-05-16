package ca.metricalsky.winston.database.repository.author

import ca.metricalsky.winston.database.entity.author.AuthorEntity
import ca.metricalsky.winston.database.view.AuthorChannelView
import ca.metricalsky.winston.database.view.AuthorDetailsView
import ca.metricalsky.winston.database.view.AuthorVideoView
import ca.metricalsky.winston.database.view.VideoStatisticsView
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface AuthorRepository: JpaRepository<AuthorEntity, String> {

    override fun count(): Long

    fun countByDisplayNameLike(displayName: String): Long

    override fun findAll(pageable: Pageable): Page<AuthorEntity>

    fun findAllByDisplayNameLike(displayName: String, pageable: Pageable): Page<AuthorEntity>

    fun findByChannelUrl(channelUrl: String): AuthorEntity?

    fun findByDisplayName(displayName: String): AuthorEntity?

    @Query("""
        SELECT c.author.id AS authorId,
            COUNT(DISTINCT v.channelId) AS channelCount,
            COUNT(DISTINCT c.videoId) AS videoCount,
            COUNT(c.id) AS totalCommentCount,
            COUNT(c.parentId) AS replyCount
        FROM CommentEntity c
            LEFT JOIN VideoEntity v ON c.videoId = v.id
        WHERE c.author.id IN :ids
        GROUP BY c.author.id
    """)
    fun findAuthorDetailsByIds(ids: Iterable<String>): List<AuthorDetailsView>

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
    fun findVideoStatisticsByAuthorId(id: String): List<VideoStatisticsView>

    @Query("""
        SELECT
            ch.title AS channelTitle,
            ch.customUrl AS channelHandle,
            COUNT(DISTINCT v.id) AS videoCount,
            COUNT(co.id) AS totalCommentCount,
            COUNT(co.parentId) AS replyCount,
            MIN(co.publishedAt) AS firstCommentedAt,
            MAX(co.publishedAt) AS lastCommentedAt
        FROM ChannelEntity ch
            JOIN VideoEntity v ON ch.id = v.channelId
            JOIN CommentEntity co ON v.id = co.videoId
        WHERE co.author.displayName = :displayName
        GROUP BY ch.id
    """)
    fun findAuthorChannelsByDisplayName(displayName: String): List<AuthorChannelView>

    @Query("""
        SELECT
            v.id AS videoId,
            v.title AS videoTitle,
            v.thumbnailUrl AS videoThumbnailUrl,
            COUNT(co.id) AS totalCommentCount,
            COUNT(co.parentId) AS replyCount,
            MIN(co.publishedAt) AS firstCommentedAt,
            MAX(co.publishedAt) AS lastCommentedAt
        FROM VideoEntity v
            JOIN CommentEntity co ON v.id = co.videoId
        WHERE co.author.displayName = :displayName
        GROUP BY v.id
    """)
    fun findAuthorVideosByDisplayName(displayName: String): List<AuthorVideoView>
}
