package ca.metricalsky.winston.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "authors")
@Getter
@Setter
public class Author {

    @Id
    private String id;

    private String displayName;

    private String channelUrl;

    private String profileImageUrl;

    @UpdateTimestamp
    private OffsetDateTime lastFetchedAt;

}
