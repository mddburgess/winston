package ca.metricalsky.winston.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.List;

import static jakarta.persistence.CascadeType.MERGE;
import static jakarta.persistence.CascadeType.PERSIST;

@Entity
@Table(name = "comments")
@Getter
@Setter
public class CommentEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "video_id")
    private String videoId;

    @Column(name = "parent_id")
    private String parentId;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private AuthorEntity author;

    @Column(name = "text_display")
    private String textDisplay;

    @Column(name = "text_original")
    private String textOriginal;

    @Column(name = "total_reply_count")
    private Long totalReplyCount;

    @Column(name = "published_at")
    private OffsetDateTime publishedAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @UpdateTimestamp
    @Column(name = "last_fetched_at")
    private OffsetDateTime lastFetchedAt;

    @OneToMany(cascade = {PERSIST, MERGE})
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private List<CommentEntity> replies;

}
