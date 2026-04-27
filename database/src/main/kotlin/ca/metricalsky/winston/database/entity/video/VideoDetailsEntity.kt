package ca.metricalsky.winston.database.entity.video

import jakarta.persistence.CascadeType
import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.Duration
import java.time.OffsetDateTime

@Entity
@Table(name = "video_details")
class VideoDetailsEntity(

    @Id
    @Column(name = "video_id")
    var videoId: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility")
    var visibility: Visibility?,

    @Column(name = "duration")
    var duration: Duration,

    @Column(name = "category")
    var category: String?,

    @ElementCollection
    @CollectionTable(
        name = "video_topics",
        joinColumns = [JoinColumn(name = "video_id", referencedColumnName = "video_id")]
    )
    @Column(name = "topic_url")
    var topics: MutableSet<String>,

    @ElementCollection
    @CollectionTable(
        name = "video_tags",
        joinColumns = [JoinColumn(name = "video_id", referencedColumnName = "video_id")]
    )
    @Column(name = "tag")
    var tags: MutableSet<String>,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "video_id", referencedColumnName = "video_id")
    var restrictions: MutableList<VideoRestrictionEntity>,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "video_id", referencedColumnName = "video_id")
    var contentRatings: MutableList<VideoContentRatingEntity>,

    @Column(name = "made_for_kids")
    var madeForKids: Boolean,

    @Column(name = "contains_synthetic_media")
    var containsSyntheticMedia: Boolean,

    @Column(name = "has_paid_product_placement")
    var hasPaidProductPlacement: Boolean,

    @OneToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinColumn(name = "video_id", referencedColumnName = "video_id")
    var recordingLocation: VideoRecordingLocationEntity?,

    @Column(name = "recorded_at")
    var recordedAt: OffsetDateTime?,

    @Column(name = "live_streamed_at")
    var liveStreamedAt: OffsetDateTime?,

    @Column(name = "view_count")
    var viewCount: Long,

    @Column(name = "like_count")
    var likeCount: Long,

    @Column(name = "comment_count")
    var commentCount: Long
) {
    enum class Visibility {
        PUBLIC,
        UNLISTED,
        PRIVATE
    }
}
