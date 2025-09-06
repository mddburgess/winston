package ca.metricalsky.winston.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "video_details")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoDetailsEntity {

    @Id
    @Column(name = "video_id")
    private String videoId;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility")
    private Visibility visibility;

    @Column(name = "duration")
    private Duration duration;

    @Column(name = "category")
    private String category;

    @ElementCollection
    @CollectionTable(
            name = "video_topics",
            joinColumns = @JoinColumn(name = "video_id", referencedColumnName = "video_id")
    )
    @Column(name = "topic_url")
    private Set<String> topics;

    @ElementCollection
    @CollectionTable(
            name = "video_tags",
            joinColumns = @JoinColumn(name = "video_id", referencedColumnName = "video_id")
    )
    @Column(name = "tag")
    private Set<String> tags;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "video_id", referencedColumnName = "video_id")
    private List<VideoRestrictionEntity> restrictions;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "video_id", referencedColumnName = "video_id")
    private List<VideoContentRatingEntity> contentRatings;

    @Column(name = "made_for_kids")
    private Boolean madeForKids;

    @Column(name = "contains_synthetic_media")
    private Boolean containsSyntheticMedia;

    @Column(name = "has_paid_product_placement")
    private Boolean hasPaidProductPlacement;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "video_id", referencedColumnName = "video_id")
    private VideoRecordingLocationEntity recordingLocation;

    @Column(name = "recorded_at")
    private OffsetDateTime recordedAt;

    @Column(name = "live_streamed_at")
    private OffsetDateTime liveStreamedAt;

    @Column(name = "view_count")
    private Long viewCount;

    @Column(name = "like_count")
    private Long likeCount;

    @Column(name = "comment_count")
    private Long commentCount;

    enum Visibility {
        PUBLIC,
        UNLISTED,
        PRIVATE
    }
}
