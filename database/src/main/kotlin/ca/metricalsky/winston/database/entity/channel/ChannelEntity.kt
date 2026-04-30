package ca.metricalsky.winston.database.entity.channel

import jakarta.persistence.*
import org.hibernate.annotations.UpdateTimestamp
import java.time.OffsetDateTime

@Entity
@Table(name = "channels")
class ChannelEntity {

    @Id
    @Column(name = "id")
    var id: String? = null

    @Column(name = "title")
    var title: String? = null

    @Column(name = "description", length = 1000)
    var description: String? = null

    @Column(name = "custom_url")
    var customUrl: String? = null

    @Column(name = "thumbnail_url")
    var thumbnailUrl: String? = null

    @Column(name = "uploads_playlist_id")
    var uploadsPlaylistId: String? = null

    @Column(name = "video_count")
    var videoCount: Long? = null

    @Column(name = "view_count")
    var viewCount: Long? = null

    @Column(name = "subscriber_count")
    var subscriberCount: Long? = null

    @Column(name = "published_at")
    var publishedAt: OffsetDateTime? = null

    @UpdateTimestamp
    @Column(name = "last_fetched_at")
    var lastFetchedAt: OffsetDateTime? = null

    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "channel_id")
    var properties: ChannelPropertiesEntity? = null

    @ElementCollection
    @CollectionTable(
        name = "channel_topics",
        joinColumns = [JoinColumn(name = "channel_id", referencedColumnName = "id")]
    )
    @Column(name = "topic_url")
    var topics: MutableSet<String?>? = null

    @ElementCollection
    @CollectionTable(
        name = "channel_keywords",
        joinColumns = [JoinColumn(name = "channel_id", referencedColumnName = "id")]
    )
    @Column(name = "keyword")
    var keywords: MutableSet<String?>? = null
}
