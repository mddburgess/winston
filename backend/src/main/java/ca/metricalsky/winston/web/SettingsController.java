package ca.metricalsky.winston.web;

import ca.metricalsky.winston.api.SettingsApi;
import ca.metricalsky.winston.api.model.PatchOperation;
import ca.metricalsky.winston.api.model.Settings;
import ca.metricalsky.winston.service.SettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SettingsController implements SettingsApi {

    private final SettingsService settingsService;

    @Override
    public ResponseEntity<Settings> getSettings() {
        var settings = settingsService.getSettings();

        return ResponseEntity.ok(settings);
    }

    @Override
    public ResponseEntity<Settings> patchSettings(List<PatchOperation> patchOperations) {
        var patchedSettings = settingsService.patchSettings(patchOperations);

        return ResponseEntity.ok(patchedSettings);
    }
}
