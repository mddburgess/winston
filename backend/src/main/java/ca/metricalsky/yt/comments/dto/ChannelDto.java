package ca.metricalsky.yt.comments.dto;

import java.time.OffsetDateTime;
import java.util.TreeSet;

public class ChannelDto {

    private String id;

    private String title;

    private String description;

    private String customUrl;

    private String thumbnailUrl;

    private TreeSet<String> topics;

    private TreeSet<String> keywords;

    private Integer videoCount;

    private OffsetDateTime publishedAt;

    private OffsetDateTime lastFetchedAt;

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

    public TreeSet<String> getTopics() {
        return topics;
    }

    public void setTopics(TreeSet<String> topics) {
        this.topics = topics;
    }

    public TreeSet<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(TreeSet<String> keywords) {
        this.keywords = keywords;
    }

    public Integer getVideoCount() {
        return videoCount;
    }

    public void setVideoCount(Integer videoCount) {
        this.videoCount = videoCount;
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

    public void setLastFetchedAt(OffsetDateTime lastFetchedAt) {
        this.lastFetchedAt = lastFetchedAt;
    }
}
