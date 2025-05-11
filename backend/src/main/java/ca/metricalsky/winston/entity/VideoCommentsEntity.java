package ca.metricalsky.winston.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "video_comments")
@Getter
@Setter
public class VideoCommentsEntity {

    @Id
    private String videoId;

    @Basic(optional = false)
    private boolean commentsDisabled;

    @Basic(optional = false)
    private long commentCount;

    @Basic(optional = false)
    private long replyCount;

    @Basic(optional = false)
    private long totalReplyCount;

    @UpdateTimestamp
    @Basic(optional = false)
    private OffsetDateTime lastFetchedAt;
}
