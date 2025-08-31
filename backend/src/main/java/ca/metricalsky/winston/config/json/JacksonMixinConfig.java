package ca.metricalsky.winston.config.json;

import ca.metricalsky.winston.api.model.PullOperation;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class JacksonMixinConfig {

    @Bean
    Jackson2ObjectMapperBuilderCustomizer addMixins() {
        return builder -> builder
                .mixIn(PullOperation.class, PullOperationMixin.class);
    }
}
