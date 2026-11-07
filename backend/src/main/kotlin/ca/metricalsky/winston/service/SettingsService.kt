package ca.metricalsky.winston.service;

import ca.metricalsky.winston.api.model.PatchOperation;
import ca.metricalsky.winston.api.model.Settings;
import ca.metricalsky.winston.database.entity.SettingsEntity;
import ca.metricalsky.winston.database.repository.SettingsRepository;
import ca.metricalsky.winston.utils.JsonPatchUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SettingsService {

    private final ConversionService conversionService;
    private final JsonPatchUtils jsonPatchUtils;
    private final SettingsRepository settingsRepository;

    public Settings getSettings() {
        var settingsEntities = settingsRepository.findAll();
        return conversionService.convert(settingsEntities, Settings.class);
    }

    public Settings patchSettings(List<PatchOperation> patchOperations) {
        var settingsEntities = patchOperations.stream()
                .map(patchOperation -> conversionService.convert(patchOperation, SettingsEntity.class))
                .toList();
        settingsRepository.saveAll(settingsEntities);

        return getSettings();
    }
}
