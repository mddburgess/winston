package ca.metricalsky.winston.database.repository.author

import ca.metricalsky.winston.database.entity.author.AuthorEntity
import jakarta.transaction.Transactional
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils
import org.springframework.stereotype.Repository

@Repository
@Transactional
class AuthorJdbcRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {
    fun saveAll(authors: Collection<AuthorEntity>) {
        if (authors.isEmpty()) {
            return
        }

        val authorIds = authors.mapNotNull { it.id }
        val idsToUpdate = selectAuthorIds(authorIds)

        val partition = authors.partition { it.id in idsToUpdate }
        val authorsToUpdate = partition.first
        val authorsToInsert = partition.second

        insertAuthors(authorsToInsert)
        updateAuthors(authorsToUpdate)
    }

    private fun selectAuthorIds(authorIds: Collection<String>): Set<String> {
        return HashSet<String>(
            jdbcTemplate.queryForList(
                "SELECT id FROM authors WHERE id IN (:ids)",
                MapSqlParameterSource("ids", authorIds),
                String::class.java
            )
        )
    }

    private fun insertAuthors(authors: Collection<AuthorEntity>) {
        jdbcTemplate.batchUpdate("""
            INSERT INTO authors (id, display_name, channel_url, profile_image_url, last_fetched_at)
            VALUES (:id, :displayName, :channelUrl, :profileImageUrl, :lastFetchedAt)
            """,
            SqlParameterSourceUtils.createBatch(authors)
        )
    }

    private fun updateAuthors(authors: Collection<AuthorEntity>) {
        jdbcTemplate.batchUpdate("""
            UPDATE authors
            SET display_name = :displayName,
                channel_url = :channelUrl,
                profile_image_url = :profileImageUrl,
                last_fetched_at = :lastFetchedAt
            WHERE id = :id
            """,
            SqlParameterSourceUtils.createBatch(authors)
        )
    }
}
