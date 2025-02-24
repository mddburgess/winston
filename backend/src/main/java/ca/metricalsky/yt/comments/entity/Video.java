package ca.metricalsky.yt.comments.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "videos")
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
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
}
