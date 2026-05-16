package ca.metricalsky.winston.database.repository.fetch

import ca.metricalsky.winston.database.entity.fetch.FetchOperationEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FetchOperationRepository : JpaRepository<FetchOperationEntity, Long>
