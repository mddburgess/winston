package ca.metricalsky.winston.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "video_recording_locations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoRecordingLocationEntity {

    @Id
    @Column(name = "video_id")
    private String videoId;

    @Column(name = "description")
    private String description;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "altitude")
    private Double altitude;
}
