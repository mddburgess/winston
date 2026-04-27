package ca.metricalsky.winston.entity;

import ca.metricalsky.winston.database.entity.channel.ChannelPropertiesEntity;
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

    @Column(name = "uploads_playlist_id")
    private String uploadsPlaylistId;

    @Column(name = "video_count")
    private Long videoCount;

    @Column(name = "view_count")
    private Long viewCount;

    @Column(name = "subscriber_count")
    private Long subscriberCount;

    @Column(name = "published_at")
    private OffsetDateTime publishedAt;

    @UpdateTimestamp
    @Column(name = "last_fetched_at")
    private OffsetDateTime lastFetchedAt;

    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "channel_id")
    private ChannelPropertiesEntity properties;

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
