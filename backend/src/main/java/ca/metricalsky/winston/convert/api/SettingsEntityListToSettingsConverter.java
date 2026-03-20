package ca.metricalsky.winston.convert.api;

import ca.metricalsky.winston.api.model.Settings;
import ca.metricalsky.winston.entity.SettingsEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SettingsEntityListToSettingsConverter
        implements Converter<List<SettingsEntity>, Settings> {

    @Lazy
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Settings convert(List<SettingsEntity> source) {
        var node = JsonNodeFactory.instance.objectNode();
        source.forEach(entity -> node.put(entity.getName(), entity.getValue()));
        return objectMapper.convertValue(node, Settings.class);
    }
}
