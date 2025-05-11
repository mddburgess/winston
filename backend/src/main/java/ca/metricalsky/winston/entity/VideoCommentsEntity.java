package ca.metricalsky.winston.entity;

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

    private boolean commentsDisabled;

    private Long commentCount;

    private Long replyCount;

    private Long totalReplyCount;

    @UpdateTimestamp
    private OffsetDateTime lastFetchedAt;
}
