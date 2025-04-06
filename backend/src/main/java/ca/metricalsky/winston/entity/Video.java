package ca.metricalsky.winston.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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

    private boolean commentsDisabled;

    private OffsetDateTime publishedAt;

    @UpdateTimestamp
    private OffsetDateTime lastFetchedAt;

}
