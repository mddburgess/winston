package ca.metricalsky.winston.entity.fetch;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "fetch_actions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FetchActionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Basic(optional = false)
    @Column(name = "fetch_operation_id")
    private Long fetchOperationId;

    @Basic(optional = false)
    @Enumerated(EnumType.STRING)
    @Column(name = "action_type")
    private Type actionType;

    @Basic(optional = false)
    @Column(name = "object_id")
    private String objectId;

    @Column(name = "published_after")
    private OffsetDateTime publishedAfter;

    @Column(name = "published_before")
    private OffsetDateTime publishedBefore;

    @Column(name = "page_token")
    private String pageToken;

    @Basic(optional = false)
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "item_count")
    private Integer itemCount;

    @Column(name = "error")
    private String error;

    @CreationTimestamp
    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "last_updated_at")
    private OffsetDateTime lastUpdatedAt;

    public enum Type {
        CHANNELS,
        VIDEOS,
        COMMENTS,
        REPLIES,
    }

    public enum Status {
        READY,
        FETCHING,
        SUCCESSFUL,
        FAILED,
    }
}
