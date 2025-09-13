package ca.metricalsky.winston.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "video_content_ratings")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoContentRatingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "video_id")
    private String videoId;

    @Column(name = "authority")
    private String authority;

    @Column(name = "rating")
    private String rating;
}
