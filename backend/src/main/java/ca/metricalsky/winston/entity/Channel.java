package ca.metricalsky.winston.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.Set;

@Entity
@Table(name = "channels")
@Getter
@Setter
public class Channel {

    @Id
    private String id;

    private String title;

    @Column(length = 1000)
    private String description;

    private String customUrl;

    private String thumbnailUrl;

    private OffsetDateTime publishedAt;

    @UpdateTimestamp
    private OffsetDateTime lastFetchedAt;

    @ElementCollection
    @CollectionTable(
            name = "channel_topics",
            joinColumns = @JoinColumn(name = "channel_id", referencedColumnName = "id")
    )
    @Column(name = "topic_url")
    private Set<String> topics;

    @ElementCollection
    @CollectionTable(
            name = "channel_keywords",
            joinColumns = @JoinColumn(name = "channel_id", referencedColumnName = "id")
    )
    @Column(name = "keyword")
    private Set<String> keywords;
}
