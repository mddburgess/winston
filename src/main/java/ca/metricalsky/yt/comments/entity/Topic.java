package ca.metricalsky.yt.comments.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Topic {

    @Id
    private String topicUrl;

    public String getTopicUrl() {
        return topicUrl;
    }

    public void setTopicUrl(String topicUrl) {
        this.topicUrl = topicUrl;
    }
}
