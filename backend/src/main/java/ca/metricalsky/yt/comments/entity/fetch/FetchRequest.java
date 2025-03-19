package ca.metricalsky.yt.comments.entity.fetch;

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

    private String fetchId;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String error;

    @CreationTimestamp
    private OffsetDateTime requestedAt;

    @UpdateTimestamp
    private OffsetDateTime lastUpdatedAt;

    public enum Status {
        ACCEPTED,
        FETCHING,
        COMPLETED,
        FAILED,
    }
}
