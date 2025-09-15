package ca.metricalsky.winston.repository;

import ca.metricalsky.winston.entity.CommentEntity;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.ListUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@Transactional
@RequiredArgsConstructor
public class CommentJdbcRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public List<CommentEntity> saveAll(List<CommentEntity> comments) {
        var replies = comments.stream()
                .map(CommentEntity::getReplies)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .toList();
        var commentsAndReplies = ListUtils.union(comments, replies);

        var lastFetchedAt = OffsetDateTime.now();
        var commentMap = commentsAndReplies.stream()
                .peek(comment -> comment.setLastFetchedAt(lastFetchedAt))
                .collect(Collectors.toMap(
                        CommentEntity::getId, Function.identity(), (first, _) -> first));

        var idsToUpdate = selectCommentIds(commentMap.keySet());
        var commentsToInsert = new ArrayList<CommentEntity>();
        var commentsToUpdate = new ArrayList<CommentEntity>();

        for (var comment : commentMap.values()) {
            if (idsToUpdate.contains(comment.getId())) {
                commentsToUpdate.add(comment);
            } else {
                commentsToInsert.add(comment);
            }
        }

        insertComments(commentsToInsert);
        updateComments(commentsToUpdate);

        return comments;
    }

    private Set<String> selectCommentIds(Collection<String> commentIds) {
        return new HashSet<>(jdbcTemplate.queryForList(
                "SELECT id FROM comments WHERE id IN (:ids)",
                new MapSqlParameterSource("ids", commentIds),
                String.class));
    }

    private void insertComments(Collection<CommentEntity> comments) {
        jdbcTemplate.batchUpdate("""
                INSERT INTO comments (id, video_id, parent_id, author_id, text_display, text_original,
                                      total_reply_count, published_at, updated_at, last_fetched_at, like_count)
                VALUES (:id, :videoId, :parentId, :author.id, :textDisplay, :textOriginal,
                        :totalReplyCount, :publishedAt, :updatedAt, :lastFetchedAt, :likeCount)
                """,
                SqlParameterSourceUtils.createBatch(comments)
        );
    }

    private void updateComments(Collection<CommentEntity> comments) {
        jdbcTemplate.batchUpdate("""
                UPDATE comments
                SET video_id = :videoId,
                    parent_id = :parentId,
                    author_id = :author.id,
                    text_display = :textDisplay,
                    text_original = :textOriginal,
                    total_reply_count = :totalReplyCount,
                    published_at = :publishedAt,
                    updated_at = :updatedAt,
                    last_fetched_at = :lastFetchedAt,
                    like_count = :likeCount
                WHERE id = :id
                """,
                SqlParameterSourceUtils.createBatch(comments)
        );
    }
}
