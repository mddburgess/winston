package ca.metricalsky.winston.database.repository.fetch

import ca.metricalsky.winston.database.entity.fetch.FetchActionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FetchActionRepository : JpaRepository<FetchActionEntity, Long>
