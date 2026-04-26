package ca.metricalsky.winston.database.repository

import ca.metricalsky.winston.database.entity.SettingsEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SettingsRepository: JpaRepository<SettingsEntity, String>
