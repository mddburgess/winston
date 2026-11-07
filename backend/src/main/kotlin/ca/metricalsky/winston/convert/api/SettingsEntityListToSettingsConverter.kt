package ca.metricalsky.winston.convert.api

import ca.metricalsky.winston.api.model.Settings
import ca.metricalsky.winston.database.entity.SettingsEntity
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import org.springframework.context.annotation.Lazy
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class SettingsEntityListToSettingsConverter(
    @Lazy private var objectMapper: ObjectMapper
) : Converter<List<SettingsEntity>, Settings?> {

    override fun convert(source: List<SettingsEntity>): Settings? {
        val node = JsonNodeFactory.instance.objectNode()
        source.forEach { node.put(it.name, it.value) }
        return objectMapper.convertValue(node, Settings::class.java)
    }
}
