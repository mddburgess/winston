package ca.metricalsky.yt.comments.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.Set;

@Entity
@Table(name = "channels")
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCustomUrl() {
        return customUrl;
    }

    public void setCustomUrl(String customUrl) {
        this.customUrl = customUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public OffsetDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(OffsetDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public OffsetDateTime getLastFetchedAt() {
        return lastFetchedAt;
    }

    public void setLastFetchedAt(OffsetDateTime lastRefreshedAt) {
        this.lastFetchedAt = lastRefreshedAt;
    }

    public Set<String> getTopics() {
        return topics;
    }

    public void setTopics(Set<String> topics) {
        this.topics = topics;
    }

    public Set<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(Set<String> keywords) {
        this.keywords = keywords;
    }
}
