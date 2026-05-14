package ca.metricalsky.winston.database.entity.video

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.sql.Restriction

@Entity
@Table(name = "video_restrictions")
class VideoRestrictionEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long,

    @Column(name = "video_id")
    var videoId: String?,

    @Enumerated(EnumType.STRING)
    @Column(name = "restriction")
    var restriction: Restriction,

    @Column(name = "country")
    var country: String,
) {
    enum class Restriction {
        ALLOWED,
        BLOCKED
    }
}
