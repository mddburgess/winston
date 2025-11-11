package ca.metricalsky.winston.convert.api;

import ca.metricalsky.winston.api.model.PatchOperation;
import ca.metricalsky.winston.entity.SettingsEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PatchOperationToSettingsEntityConverter
        implements Converter<PatchOperation, SettingsEntity> {

    @Override
    public SettingsEntity convert(PatchOperation patchOperation) {
        var settingsEntity = new SettingsEntity();
        settingsEntity.setName(patchOperation.getPath());

        if (patchOperation.getOp() != PatchOperation.OpEnum.REMOVE && patchOperation.getValue() != null) {
            settingsEntity.setValue(patchOperation.getValue().toString());
        }

        return settingsEntity;
    }
}
