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
@Table(name = "fetch_requests")
@Getter
@Setter
public class FetchRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private FetchType fetchType;

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

    public enum FetchType {
        CHANNEL,
        VIDEOS,
        COMMENTS,
    }

    public enum Status {
        FETCHING,
        COMPLETED,
        FAILED,
    }
}
