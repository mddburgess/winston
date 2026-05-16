package ca.metricalsky.winston.database.entity.fetch

import jakarta.persistence.Basic
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.OffsetDateTime

@Entity
@Table(name = "fetch_requests")
class FetchRequestEntity(

    @OneToMany(cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "fetch_request_id", referencedColumnName = "id")
    var operations: MutableList<FetchOperationEntity> = mutableListOf(),

    @Basic(optional = false)
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    var status: Status = Status.ACCEPTED,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null

    @CreationTimestamp
    @Column(name = "created_at")
    var createdAt: OffsetDateTime? = null

    @UpdateTimestamp
    @Column(name = "last_updated_at")
    var lastUpdatedAt: OffsetDateTime? = null

    enum class Status {
        ACCEPTED,
        FETCHING,
        PAUSED,
        COMPLETED,
    }
}
