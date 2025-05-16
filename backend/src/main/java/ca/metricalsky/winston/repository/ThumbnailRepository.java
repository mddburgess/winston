package ca.metricalsky.winston.repository;

import ca.metricalsky.winston.entity.ThumbnailEntity;
import ca.metricalsky.winston.entity.view.ThumbnailLookupView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ThumbnailRepository extends JpaRepository<ThumbnailEntity, String> {

    @Query("""
            SELECT c.thumbnailUrl AS thumbnailUrl, t AS thumbnail
            FROM ChannelEntity c
                LEFT JOIN ThumbnailEntity t ON c.id = t.id
            WHERE c.id = :channelId
            """)
    ThumbnailLookupView lookupByChannelId(String channelId);

    @Query("""
            SELECT v.thumbnailUrl AS thumbnailUrl, t AS thumbnail
            FROM VideoEntity v
                LEFT JOIN ThumbnailEntity t ON v.id = t.id
            WHERE v.id = :videoId
            """)
    ThumbnailLookupView lookupByVideoId(String videoId);

    @Query("""
            SELECT a.profileImageUrl AS thumbnailUrl, t AS thumbnail
            FROM AuthorEntity a
                LEFT JOIN ThumbnailEntity t ON a.id = t.id
            WHERE a.id = :authorId
            """)
    ThumbnailLookupView lookupByAuthorId(String authorId);
}
