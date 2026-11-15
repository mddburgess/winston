package ca.metricalsky.winston.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.hibernate.validator.spi.nodenameprovider.JavaBeanProperty
import org.hibernate.validator.spi.nodenameprovider.Property
import org.hibernate.validator.spi.nodenameprovider.PropertyNodeNameProvider
import org.springframework.stereotype.Component

@Component
class JacksonPropertyNodeNameProvider(
    private val objectMapper: ObjectMapper,
) : PropertyNodeNameProvider {

    override fun getName(property: Property) = when (property) {
        is JavaBeanProperty -> getJavaBeanPropertyName(property)
        else -> property.name
    }

    private fun getJavaBeanPropertyName(property: JavaBeanProperty): String {
        val javaType = objectMapper.constructType(property.declaringClass)
        val beanDescription = objectMapper.serializationConfig.introspect(javaType)

        return beanDescription.findProperties()
            .filter { it.internalName == property.name }
            .map { it.name }
            .firstOrNull() ?: property.name
    }
}
