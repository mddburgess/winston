package ca.metricalsky.winston.convert.api

import ca.metricalsky.winston.api.model.PatchOperation
import ca.metricalsky.winston.database.entity.SettingsEntity
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class PatchOperationToSettingsEntityConverter : Converter<PatchOperation, SettingsEntity> {

    override fun convert(patchOperation: PatchOperation) = SettingsEntity(
        name = patchOperation.path,
        value = when {
            patchOperation.op == PatchOperation.OpEnum.REMOVE -> null
            else -> patchOperation.value?.toString()
        },
    )
}
