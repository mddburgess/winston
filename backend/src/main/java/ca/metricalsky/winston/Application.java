package ca.metricalsky.winston;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableAsync
@ConfigurationPropertiesScan("ca.metricalsky.winston.config")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
