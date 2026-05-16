package ca.metricalsky.winston.database.entity.video

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "video_recording_locations")
class VideoRecordingLocationEntity(

    @Id
    @Column(name = "video_id")
    var videoId: String,

    @Column(name = "description")
    var description: String?,

    @Column(name = "latitude")
    var latitude: Double?,

    @Column(name = "longitude")
    var longitude: Double?,

    @Column(name = "altitude")
    var altitude: Double?,
)
