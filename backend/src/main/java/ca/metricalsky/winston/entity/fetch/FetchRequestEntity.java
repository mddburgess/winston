package ca.metricalsky.winston.entity.fetch;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "fetch_requests")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FetchRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "fetch_request_id", referencedColumnName = "id")
    private List<FetchOperationEntity> operations;

    @Builder.Default
    @Basic(optional = false)
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.ACCEPTED;

    @CreationTimestamp
    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "last_updated_at")
    private OffsetDateTime lastUpdatedAt;

    public enum Status {
        ACCEPTED,
        FETCHING,
        PAUSED,
        COMPLETED,
    }
}
