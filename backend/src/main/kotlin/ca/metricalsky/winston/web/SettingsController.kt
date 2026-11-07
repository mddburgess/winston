package ca.metricalsky.winston.web

import ca.metricalsky.winston.api.SettingsApi
import ca.metricalsky.winston.api.model.PatchOperation
import ca.metricalsky.winston.api.model.Settings
import ca.metricalsky.winston.service.SettingsService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class SettingsController(
    private val settingsService: SettingsService,
) : SettingsApi {

    override fun getSettings(): ResponseEntity<Settings> {
        val settings = settingsService.getSettings()

        return ResponseEntity.ok(settings)
    }

    override fun patchSettings(
        patchOperations: List<PatchOperation>?
    ): ResponseEntity<Settings> {
        settingsService.patchSettings(patchOperations.orEmpty())
        val settings = settingsService.getSettings()

        return ResponseEntity.ok(settings)
    }
}
