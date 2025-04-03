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
import java.time.format.DateTimeFormatter;

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

    public void setPublishedAfter(String publishedAfter) {
        this.publishedAfter = publishedAfter;
    }

    public void setPublishedAfter(OffsetDateTime publishedAfter) {
        this.publishedAfter = formatDate(publishedAfter);
    }

    public void setPublishedBefore(String publishedBefore) {
        this.publishedBefore = publishedBefore;
    }

    public void setPublishedBefore(OffsetDateTime publishedBefore) {
        this.publishedBefore = formatDate(publishedBefore);
    }

    private static String formatDate(OffsetDateTime date) {
        return date != null ? DateTimeFormatter.ISO_INSTANT.format(date) : null;
    }

    public enum RequestType {
        CHANNELS,
        ACTIVITIES,
        COMMENTS,
        REPLIES,
    }
}
