package ca.metricalsky.winston.entity.fetch;

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
public class YouTubeRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long fetchActionId;

    @Enumerated(EnumType.STRING)
    private RequestType requestType;

    private String objectId;

    private String publishedAfter;

    private String publishedBefore;

    private String pageToken;

    private OffsetDateTime requestedAt;

    private Integer httpStatus;

    private Integer itemCount;

    private String error;

    private OffsetDateTime respondedAt;

    public enum RequestType {
        CHANNELS,
        ACTIVITIES,
        COMMENTS,
        REPLIES,
    }
}
