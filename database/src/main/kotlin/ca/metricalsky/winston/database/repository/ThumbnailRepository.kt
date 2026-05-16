package ca.metricalsky.winston.database.repository

import ca.metricalsky.winston.database.entity.ThumbnailEntity
import ca.metricalsky.winston.database.view.ThumbnailLookupView
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ThumbnailRepository: JpaRepository<ThumbnailEntity, String> {

    @Query("""
        SELECT c.thumbnailUrl AS thumbnailUrl, t AS thumbnail
        FROM ChannelEntity c
            LEFT JOIN ThumbnailEntity t ON c.id = t.id
        WHERE c.id = :channelId
    """)
    fun lookupByChannelId(channelId: String): ThumbnailLookupView

    @Query("""
        SELECT v.thumbnailUrl AS thumbnailUrl, t AS thumbnail
        FROM VideoEntity v
            LEFT JOIN ThumbnailEntity t ON v.id = t.id
        WHERE v.id = :videoId
    """)
    fun lookupByVideoId(videoId: String): ThumbnailLookupView

    @Query("""
        SELECT a.profileImageUrl AS thumbnailUrl, t AS thumbnail
        FROM AuthorEntity a
            LEFT JOIN ThumbnailEntity t ON a.id = t.id
        WHERE a.id = :authorId
    """)
    fun lookupByAuthorId(authorId: String): ThumbnailLookupView
}
