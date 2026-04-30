package ca.metricalsky.winston.database.entity.comment

import ca.metricalsky.winston.database.entity.author.AuthorEntity
import jakarta.persistence.*
import org.hibernate.annotations.UpdateTimestamp
import java.time.OffsetDateTime

@Entity
@Table(name = "comments")
class CommentEntity {

    @Id
    @Column(name = "id")
    var id: String? = null

    @Column(name = "video_id")
    var videoId: String? = null

    @Column(name = "parent_id")
    var parentId: String? = null

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    var author: AuthorEntity? = null

    @Column(name = "text_display")
    var textDisplay: String? = null

    @Column(name = "text_original")
    var textOriginal: String? = null

    @Column(name = "like_count")
    var likeCount: Long? = null

    @Column(name = "total_reply_count")
    var totalReplyCount: Long? = null

    @Column(name = "published_at")
    var publishedAt: OffsetDateTime? = null

    @Column(name = "updated_at")
    var updatedAt: OffsetDateTime? = null

    @UpdateTimestamp
    @Column(name = "last_fetched_at")
    var lastFetchedAt: OffsetDateTime? = null

    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "comment_id")
    var properties: CommentPropertiesEntity? = null

    @OneToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    var replies: MutableList<CommentEntity?>? = null
}
