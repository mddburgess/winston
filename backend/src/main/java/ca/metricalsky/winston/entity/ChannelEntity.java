package ca.metricalsky.winston.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.Set;

@Entity
@Table(name = "channels")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChannelEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "title")
    private String title;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "custom_url")
    private String customUrl;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "published_at")
    private OffsetDateTime publishedAt;

    @UpdateTimestamp
    @Column(name = "last_fetched_at")
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
