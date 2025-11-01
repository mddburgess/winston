package ca.metricalsky.winston.repository;

import ca.metricalsky.winston.entity.AuthorEntity;
import lombok.RequiredArgsConstructor;
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
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@Transactional
@RequiredArgsConstructor
public class AuthorJdbcRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public void saveAll(List<AuthorEntity> authors) {
        if (authors.isEmpty()) {
            return;
        }

        var lastFetchedAt = OffsetDateTime.now();
        var authorMap = authors.stream()
                .peek(author -> author.setLastFetchedAt(lastFetchedAt))
                .collect(Collectors.toMap(
                        AuthorEntity::getId, Function.identity(), (first, _) -> first));

        var idsToUpdate = selectAuthorIds(authorMap.keySet());
        var authorsToInsert = new ArrayList<AuthorEntity>();
        var authorsToUpdate = new ArrayList<AuthorEntity>();

        for (var author : authorMap.values()) {
            if (idsToUpdate.contains(author.getId())) {
                authorsToUpdate.add(author);
            } else {
                authorsToInsert.add(author);
            }
        }

        insertAuthors(authorsToInsert);
        updateAuthors(authorsToUpdate);
    }

    private Set<String> selectAuthorIds(Collection<String> authorIds) {
        return new HashSet<>(jdbcTemplate.queryForList(
                "SELECT id FROM authors WHERE id IN (:ids)",
                new MapSqlParameterSource("ids", authorIds),
                String.class));
    }

    private void insertAuthors(Collection<AuthorEntity> authors) {
        jdbcTemplate.batchUpdate("""
                INSERT INTO authors (id, display_name, channel_url, profile_image_url, last_fetched_at)
                VALUES (:id, :displayName, :channelUrl, :profileImageUrl, :lastFetchedAt)
                """,
                SqlParameterSourceUtils.createBatch(authors)
        );
    }

    private void updateAuthors(Collection<AuthorEntity> authors) {
        jdbcTemplate.batchUpdate("""
                UPDATE authors
                SET display_name = :displayName,
                    channel_url = :channelUrl,
                    profile_image_url = :profileImageUrl,
                    last_fetched_at = :lastFetchedAt
                WHERE id = :id
                """,
                SqlParameterSourceUtils.createBatch(authors)
        );
    }
}
