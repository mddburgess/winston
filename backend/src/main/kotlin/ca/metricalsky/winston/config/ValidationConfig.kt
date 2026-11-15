package ca.metricalsky.winston.config

import org.hibernate.validator.HibernateValidatorConfiguration
import org.springframework.boot.autoconfigure.validation.ValidationConfigurationCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ValidationConfig(
    private val propertyNodeNameProvider: JacksonPropertyNodeNameProvider,
) {
    @Bean
    fun validationConfigCustomizer() = ValidationConfigurationCustomizer {
        if (it is HibernateValidatorConfiguration) {
            it.propertyNodeNameProvider(propertyNodeNameProvider)
        }
    }
}
