package ca.metricalsky.winston.entity.fetch;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "fetch_actions")
@Getter
@Setter
public class FetchAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long fetchRequestId;

    @Enumerated(EnumType.STRING)
    private ActionType actionType;

    private String objectId;

    private OffsetDateTime publishedAfter;

    private OffsetDateTime publishedBefore;

    private String pageToken;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Integer itemCount;

    private String error;

    @CreationTimestamp
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    private OffsetDateTime lastUpdatedAt;

    public enum ActionType {
        CHANNELS,
        VIDEOS,
        COMMENTS,
        REPLIES,
    }

    public enum Status {
        FETCHING,
        COMPLETED,
        FAILED,
    }
}
