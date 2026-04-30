package ca.metricalsky.winston.database.repository.fetch

import ca.metricalsky.winston.database.entity.fetch.YouTubeRequestEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime

@Repository
interface YouTubeRequestRepository : JpaRepository<YouTubeRequestEntity, Long> {

    fun countAllByRequestedAtAfter(date: OffsetDateTime): Int
}
