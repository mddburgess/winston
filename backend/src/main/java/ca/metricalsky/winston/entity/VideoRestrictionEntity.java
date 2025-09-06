package ca.metricalsky.winston.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "video_restrictions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoRestrictionEntity {

    @Id
    @Column(name = "video_id")
    private String videoId;

    @Enumerated(EnumType.STRING)
    @Column(name = "restriction")
    private Restriction restriction;

    @Column(name = "country")
    private String country;

    enum Restriction {
        ALLOWED,
        BLOCKED,
    }
}
