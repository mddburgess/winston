package ca.metricalsky.winston.database.entity.video

import jakarta.persistence.*
import org.hibernate.annotations.UpdateTimestamp
import java.time.OffsetDateTime

@Entity
@Table(name = "video_comments")
class VideoCommentsEntity(

    @Id
    @Column(name = "video_id")
    var videoId: String,

    @Basic(optional = false)
    @Enumerated(EnumType.STRING)
    @Column(name = "comment_status")
    var commentStatus: CommentStatus = CommentStatus.ENABLED,

    @Basic(optional = false)
    @Column(name = "comment_count")
    var commentCount: Long,

    @Basic(optional = false)
    @Column(name = "reply_count")
    var replyCount: Long,

    @Basic(optional = false)
    @Column(name = "total_reply_count")
    var totalReplyCount: Long,
) {
    @UpdateTimestamp
    @Column(name = "last_fetched_at")
    var lastFetchedAt: OffsetDateTime? = null

    @Deprecated(message = "Remove after migration")
    val commentsDisabled
        get() = commentStatus == CommentStatus.DISABLED

    enum class CommentStatus {
        ENABLED,
        RESTRICTED,
        DISABLED,
    }
}
