package ca.metricalsky.winston.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "video_comments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoCommentsEntity {

    @Id
    @Column(name = "video_id")
    private String videoId;

    @Basic(optional = false)
    @Column(name = "comments_disabled")
    private boolean commentsDisabled;

    @Basic(optional = false)
    @Column(name = "comment_count")
    private long commentCount;

    @Basic(optional = false)
    @Column(name = "reply_count")
    private long replyCount;

    @Basic(optional = false)
    @Column(name = "total_reply_count")
    private long totalReplyCount;

    @UpdateTimestamp
    @Column(name = "last_fetched_at")
    private OffsetDateTime lastFetchedAt;
}
