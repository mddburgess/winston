package ca.metricalsky.winston.database.repository.fetch

import ca.metricalsky.winston.database.entity.fetch.FetchRequestEntity
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.lang.NonNull
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface FetchRequestRepository : JpaRepository<FetchRequestEntity, Long> {

    @EntityGraph(attributePaths = ["operations"])
    override fun findById(id: Long): Optional<FetchRequestEntity>
}
