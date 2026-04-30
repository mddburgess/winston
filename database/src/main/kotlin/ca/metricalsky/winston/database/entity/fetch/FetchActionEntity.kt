package ca.metricalsky.winston.database.entity.fetch

import jakarta.persistence.Basic
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.OffsetDateTime


@Entity
@Table(name = "fetch_actions")
class FetchActionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null

    @Basic(optional = false)
    @Column(name = "fetch_operation_id")
    var fetchOperationId: Long? = null

    @Basic(optional = false)
    @Enumerated(EnumType.STRING)
    @Column(name = "action_type")
    var actionType: Type? = null

    @Basic(optional = false)
    @Column(name = "object_id")
    var objectId: String? = null

    @Column(name = "published_after")
    var publishedAfter: OffsetDateTime? = null

    @Column(name = "published_before")
    var publishedBefore: OffsetDateTime? = null

    @Column(name = "page_token")
    var pageToken: String? = null

    @Basic(optional = false)
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    var status = Status.READY

    @Column(name = "item_count")
    var itemCount: Int? = null

    @Column(name = "error")
    var error: String? = null

    @CreationTimestamp
    @Column(name = "created_at")
    private var createdAt: OffsetDateTime? = null

    @UpdateTimestamp
    @Column(name = "last_updated_at")
    private var lastUpdatedAt: OffsetDateTime? = null

    enum class Type {
        CHANNELS,
        VIDEOS,
        COMMENTS,
        REPLIES,
    }

    enum class Status {
        READY,
        FETCHING,
        SUCCESSFUL,
        FAILED,
    }
}
