package ca.metricalsky.winston.repository;

import ca.metricalsky.winston.entity.AuthorEntity;
import ca.metricalsky.winston.entity.view.AuthorChannelView;
import ca.metricalsky.winston.entity.view.AuthorDetailsView;
import ca.metricalsky.winston.entity.view.VideoStatisticsView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorEntity, String> {

    @Override
    long count();

    long countByDisplayNameLike(String displayName);

    @Override
    Page<AuthorEntity> findAll(Pageable pageable);

    Page<AuthorEntity> findAllByDisplayNameLike(String displayName, Pageable pageable);

    Optional<AuthorEntity> findByChannelUrl(String channelUrl);

    Optional<AuthorEntity> findByDisplayName(String displayName);

    @Query("""
            SELECT c.author.id AS authorId,
                COUNT(DISTINCT v.channelId) AS channelCount,
                COUNT(DISTINCT c.videoId) AS videoCount,
                COUNT(c.id) AS totalCommentCount,
                COUNT(c.parentId) AS replyCount
            FROM CommentEntity c
                LEFT JOIN VideoEntity v ON c.videoId = v.id
            WHERE c.author.id IN :ids
            GROUP BY c.author.id
            """)
    List<AuthorDetailsView> findAuthorDetailsByIds(Iterable<String> ids);

    @Query("""
            SELECT
                v.channelId AS channelId,
                v.id AS videoId,
                COUNT(c.id) - COUNT(c.parentId) AS commentCount,
                COUNT(c.parentId) AS replyCount,
                MAX(c.publishedAt) AS lastCommentedAt
            FROM CommentEntity c
                LEFT JOIN VideoEntity v ON c.videoId = v.id
            WHERE c.author.id = :id
            GROUP BY v.id
            """)
    List<VideoStatisticsView> findVideoStatisticsByAuthorId(String id);

    @Query("""
            SELECT
                ch.title AS channelTitle,
                ch.customUrl AS channelHandle,
                COUNT(DISTINCT v.id) AS videoCount,
                COUNT(co.id) AS totalCommentCount,
                COUNT(co.parentId) AS replyCount,
                MIN(co.publishedAt) AS firstCommentedAt,
                MAX(co.publishedAt) AS lastCommentedAt
            FROM ChannelEntity ch
                JOIN VideoEntity v ON ch.id = v.channelId
                JOIN CommentEntity co ON v.id = co.videoId
            WHERE co.author.displayName = :displayName
            GROUP BY ch.id
            """)
    List<AuthorChannelView> findAuthorChannelsByDisplayName(String displayName);
}
