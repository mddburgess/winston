package ca.metricalsky.winston.database.repository.channel

import ca.metricalsky.winston.database.entity.channel.ChannelEntity
import ca.metricalsky.winston.database.view.ChannelVideoStatisticsView
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ChannelRepository : JpaRepository<ChannelEntity, String> {

    @EntityGraph(attributePaths = ["keywords", "properties", "topics"])
    override fun findAll(): List<ChannelEntity>

    @EntityGraph(attributePaths = ["keywords", "properties", "topics"])
    override fun findById(id: String): Optional<ChannelEntity>

    @EntityGraph(attributePaths = ["keywords", "properties", "topics"])
    fun findByCustomUrl(customUrl: String): Optional<ChannelEntity>

    @Query("""
        SELECT c
        FROM ChannelEntity c
            LEFT JOIN ChannelPropertiesEntity cp ON c.id = cp.channelId
        WHERE cp.archived IS NULL OR cp.archived = false
    """)
    @EntityGraph(attributePaths = ["keywords", "properties", "topics"])
    fun findAllUnarchived(): List<ChannelEntity>

    @Query("SELECT c.id FROM ChannelEntity c WHERE c.customUrl = :customUrl")
    fun findIdByCustomUrl(customUrl: String): String?

    @Query("""
        SELECT
            c.customUrl AS channelHandle,
            c.id AS channelId,
            c.publishedAt AS channelPublishedAt,
            c.videoCount AS channelVideoCount,
            COUNT(v.id) AS videoCount,
            MAX(v.publishedAt) AS latestVideoPublishedAt
        FROM ChannelEntity c
        LEFT JOIN VideoEntity v ON c.id = v.channelId
        WHERE c.customUrl = :customUrl
        GROUP BY c.id
    """)
    fun findChannelVideoStatisticsByCustomUrl(customUrl: String): Optional<ChannelVideoStatisticsView>
}
