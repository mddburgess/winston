package ca.metricalsky.winston.database.entity.author

import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.Table
import org.hibernate.annotations.UpdateTimestamp
import java.time.OffsetDateTime

@Entity
@Table(name = "authors")
class AuthorEntity(

    @Id
    @Column(name = "id")
    var id: String?,

    @Column(name = "display_name")
    var displayName: String? = null,

    @Column(name = "channel_url")
    var channelUrl: String? = null,

    @Column(name = "profile_image_url")
    var profileImageUrl: String? = null,

    @ElementCollection
    @CollectionTable(
        name = "author_aliases",
        joinColumns = [JoinColumn(name = "author_id", referencedColumnName = "id")]
    )
    @Column(name = "author_alias")
    var aliases: Set<String> = setOf()
) {
    @UpdateTimestamp
    @Column(name = "last_fetched_at")
    var lastFetchedAt: OffsetDateTime? = null
}
