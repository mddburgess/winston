package ca.metricalsky.winston.config.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.spi.nodenameprovider.JavaBeanProperty;
import org.hibernate.validator.spi.nodenameprovider.Property;
import org.hibernate.validator.spi.nodenameprovider.PropertyNodeNameProvider;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JacksonPropertyNodeNameProvider
        implements PropertyNodeNameProvider {

    private final ObjectMapper objectMapper;

    @Override
    public String getName(Property property) {
        if (property instanceof JavaBeanProperty beanProperty) {
            return getJavaBeanPropertyName(beanProperty);
        }
        return getDefaultName(property);
    }

    private String getJavaBeanPropertyName(JavaBeanProperty property) {
        var javaType = objectMapper.constructType(property.getDeclaringClass());
        var beanDescription = objectMapper.getSerializationConfig().introspect(javaType);

        return beanDescription.findProperties()
                .stream()
                .filter(def -> def.getInternalName().equals(property.getName()))
                .map(BeanPropertyDefinition::getName)
                .findFirst()
                .orElse(getDefaultName(property));
    }

    private String getDefaultName(Property property) {
        return property.getName();
    }
}
