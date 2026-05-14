package ca.metricalsky.winston.database.entity.video

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "video_content_ratings")
class VideoContentRatingEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long,

    @Column(name = "video_id")
    var videoId: String?,

    @Column(name = "authority")
    var authority: String,

    @Column(name = "rating")
    var rating: String,
)
