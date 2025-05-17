package ca.metricalsky.winston.entity.fetch;

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
    private Long id;

    @Column(name = "fetch_request_id")
    private Long fetchRequestId;

    @Enumerated(EnumType.STRING)
    private Type operationType;

    private String objectId;

    private String mode;

    private OffsetDateTime publishedAfter;

    private OffsetDateTime publishedBefore;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String error;

    @CreationTimestamp
    private OffsetDateTime createdAt;

    @UpdateTimestamp
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
