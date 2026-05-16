package ca.metricalsky.winston.database.entity.video

import jakarta.persistence.*
import org.hibernate.annotations.UpdateTimestamp
import java.time.OffsetDateTime

@Entity
@Table(name = "videos")
class VideoEntity(

    @Id
    @Column(name = "id")
    var id: String? = null,

    @Basic(optional = false)
    @Column(name = "channel_id")
    var channelId: String? = null,

    @Column(name = "title")
    var title: String? = null,

    @Column(name = "description")
    var description: String? = null,

    @Column(name = "thumbnail_url")
    var thumbnailUrl: String? = null,

    @Column(name = "published_at")
    var publishedAt: OffsetDateTime? = null,
) {
    @UpdateTimestamp
    @Column(name = "last_fetched_at")
    var lastFetchedAt: OffsetDateTime? = null

    @OneToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinColumn(name = "id", referencedColumnName = "video_id")
    var details: VideoDetailsEntity? = null

    @OneToOne(cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "id", referencedColumnName = "video_id")
    var comments: VideoCommentsEntity? = null
}
