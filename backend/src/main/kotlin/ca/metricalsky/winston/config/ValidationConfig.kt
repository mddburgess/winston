package ca.metricalsky.winston.config.validation;

import lombok.RequiredArgsConstructor;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidationConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ValidationConfig {

    private final JacksonPropertyNodeNameProvider propertyNodeNameProvider;

    @Bean
    public ValidationConfigurationCustomizer validationConfigCustomizer() {
        return config -> {
            if (config instanceof HibernateValidatorConfiguration hibernateConfig) {
                hibernateConfig.propertyNodeNameProvider(propertyNodeNameProvider);
            }
        };
    }
}
