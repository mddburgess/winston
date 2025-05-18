package ca.metricalsky.winston.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "authors")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "channel_url")
    private String channelUrl;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @UpdateTimestamp
    @Column(name = "last_fetched_at")
    private OffsetDateTime lastFetchedAt;

}
