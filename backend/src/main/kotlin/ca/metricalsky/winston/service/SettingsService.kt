package ca.metricalsky.winston.service

import ca.metricalsky.winston.api.model.PatchOperation
import ca.metricalsky.winston.api.model.Settings
import ca.metricalsky.winston.database.entity.SettingsEntity
import ca.metricalsky.winston.database.repository.SettingsRepository
import lombok.RequiredArgsConstructor
import org.springframework.core.convert.ConversionService
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class SettingsService(
    private val conversionService: ConversionService,
    private val settingsRepository: SettingsRepository,
) {

    fun getSettings(): Settings? {
        val settingsEntities = settingsRepository.findAll()
        return conversionService.convert(settingsEntities, Settings::class.java)
    }

    fun patchSettings(patchOperations: List<PatchOperation>) {
        val settingsEntities = patchOperations.map {
            conversionService.convert(it, SettingsEntity::class.java)
        }
        settingsRepository.saveAll(settingsEntities)
    }
}
