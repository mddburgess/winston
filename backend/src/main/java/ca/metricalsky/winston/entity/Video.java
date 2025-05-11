package ca.metricalsky.winston.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "videos")
@Getter
@Setter
public class Video {

    @Id
    private String id;

    private String channelId;

    private String title;

    private String description;

    private String thumbnailUrl;

    private OffsetDateTime publishedAt;

    @UpdateTimestamp
    private OffsetDateTime lastFetchedAt;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id", referencedColumnName = "video_id")
    private VideoCommentsEntity comments;
}
