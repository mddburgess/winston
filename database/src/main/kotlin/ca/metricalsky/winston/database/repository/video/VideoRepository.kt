package ca.metricalsky.winston.database.repository.video

import ca.metricalsky.winston.database.entity.video.VideoEntity
import ca.metricalsky.winston.database.view.ChannelVideoView
import ca.metricalsky.winston.database.view.VideoCountView
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime
import java.util.*

@Repository
interface VideoRepository : JpaRepository<VideoEntity, String> {

    fun countByChannelId(channelId: String): Int

    @EntityGraph(attributePaths = ["comments"])
    fun findPageByChannelId(channelId: String, pageable: Pageable): MutableList<VideoEntity>

    @Query("""
        SELECT c.id AS channelId, COUNT(v.id) AS videos
        FROM ChannelEntity c
            JOIN VideoEntity v ON c.id = v.channelId
        GROUP BY c.id
    """)
    fun countAllGroupByChannelId(): MutableList<VideoCountView>

    @Query("SELECT MAX(v.publishedAt) FROM VideoEntity v WHERE v.channelId = :channelId")
    fun findLastPublishedAtForChannelId(channelId: String): Optional<OffsetDateTime>

    @Query("""
        SELECT c AS channel, v AS video
        FROM ChannelEntity c JOIN VideoEntity v ON c.id = v.channelId
        WHERE v.id = :videoId
    """)
    fun findChannelVideoById(videoId: String): Optional<ChannelVideoView>

    @Query("""
        SELECT DISTINCT ch AS channel, v AS video
        FROM ChannelEntity ch
            JOIN VideoEntity v ON ch.id = v.channelId
            JOIN CommentEntity co ON v.id = co.videoId
        WHERE co.author.displayName = :authorDisplayName
        ORDER BY v.publishedAt DESC
    """)
    @EntityGraph(attributePaths = ["comments"])
    fun findAllChannelVideosByAuthorDisplayName(authorDisplayName: String): MutableList<ChannelVideoView>
}
