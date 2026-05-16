package ca.metricalsky.winston.database.entity.fetch

import jakarta.persistence.*
import java.time.OffsetDateTime

@Entity
@Table(name = "youtube_requests")
class YouTubeRequestEntity(

    @Basic(optional = false)
    @Column(name = "fetch_action_id")
    var fetchActionId: Long? = null,

    @Basic(optional = false)
    @Enumerated(EnumType.STRING)
    @Column(name = "request_type")
    var requestType: RequestType,

    @Basic(optional = false)
    @Column(name = "object_id")
    var objectId: String,

    @Basic(optional = false)
    @Column(name = "requested_at")
    var requestedAt: OffsetDateTime = OffsetDateTime.now(),
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null

    @Column(name = "published_after")
    var publishedAfter: String? = null

    @Column(name = "published_before")
    var publishedBefore: String? = null

    @Column(name = "page_token")
    var pageToken: String? = null

    @Column(name = "http_status")
    var httpStatus: Int? = null

    @Column(name = "item_count")
    var itemCount: Int? = null

    @Column(name = "error")
    var error: String? = null

    @Column(name = "responded_at")
    var respondedAt: OffsetDateTime? = null

    enum class RequestType {
        ACTIVITIES,
        CHANNELS,
        COMMENTS,
        PLAYLIST_ITEMS,
        REPLIES,
        VIDEOS,
    }
}
