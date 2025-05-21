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
@Table(name = "fetch_operations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FetchOperationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "fetch_request_id")
    private Long fetchRequestId;

    @Basic(optional = false)
    @Enumerated(EnumType.STRING)
    @Column(name = "operation_type")
    private Type operationType;

    @Basic(optional = false)
    @Column(name = "object_id")
    private String objectId;

    @Column(name = "mode")
    private String mode;

    @Column(name = "published_after")
    private OffsetDateTime publishedAfter;

    @Column(name = "published_before")
    private OffsetDateTime publishedBefore;

    @Builder.Default
    @Basic(optional = false)
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.READY;

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
