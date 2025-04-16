package ca.metricalsky.winston.repository;

import ca.metricalsky.winston.entity.Thumbnail;
import ca.metricalsky.winston.entity.view.ThumbnailLookup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ThumbnailRepository extends JpaRepository<Thumbnail, String> {

    @Query("""
            SELECT c.thumbnailUrl AS thumbnailUrl, t AS thumbnail
            FROM Channel c
                LEFT JOIN Thumbnail t ON c.id = t.id
            WHERE c.id = :channelId
            """)
    ThumbnailLookup lookupByChannelId(String channelId);

    @Query("""
            SELECT v.thumbnailUrl AS thumbnailUrl, t AS thumbnail
            FROM Video v
                LEFT JOIN Thumbnail t ON v.id = t.id
            WHERE v.id = :videoId
            """)
    ThumbnailLookup lookupByVideoId(String videoId);

    @Query("""
            SELECT a.profileImageUrl AS thumbnailUrl, t AS thumbnail
            FROM Author a
                LEFT JOIN Thumbnail t ON a.id = t.id
            WHERE a.id = :authorId
            """)
    ThumbnailLookup lookupByAuthorId(String authorId);
}
