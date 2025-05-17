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

import java.time.OffsetDateTime;

@Entity
@Table(name = "youtube_requests")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YouTubeRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Basic(optional = false)
    @Column(name = "fetch_action_id")
    private Long fetchActionId;

    @Basic(optional = false)
    @Enumerated(EnumType.STRING)
    @Column(name = "request_type")
    private RequestType requestType;

    @Basic(optional = false)
    @Column(name = "object_id")
    private String objectId;

    @Column(name = "published_after")
    private String publishedAfter;

    @Column(name = "published_before")
    private String publishedBefore;

    @Column(name = "page_token")
    private String pageToken;

    @Basic(optional = false)
    @Column(name = "requested_at")
    private OffsetDateTime requestedAt;

    @Column(name = "http_status")
    private Integer httpStatus;

    @Column(name = "item_count")
    private Integer itemCount;

    @Column(name = "error")
    private String error;

    @Column(name = "responded_at")
    private OffsetDateTime respondedAt;

    public enum RequestType {
        CHANNELS,
        ACTIVITIES,
        COMMENTS,
        REPLIES,
    }
}
