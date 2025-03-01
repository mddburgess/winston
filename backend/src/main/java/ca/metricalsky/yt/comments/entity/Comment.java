package ca.metricalsky.yt.comments.entity;

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
public class Comment {

    @Id
    private String id;

    private String videoId;

    private String parentId;

    @ManyToOne
    private Author author;

    private String textDisplay;

    private String textOriginal;

    private Long replyCount;

    private OffsetDateTime publishedAt;

    private OffsetDateTime updatedAt;

    @UpdateTimestamp
    private OffsetDateTime lastFetchedAt;

    @OneToMany(cascade = {PERSIST, MERGE})
    @JoinColumn(name = "parentId", referencedColumnName = "id")
    private List<Comment> replies;

}
