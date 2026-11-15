package ca.metricalsky.winston

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.retry.annotation.EnableRetry
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableAsync
@EnableRetry
@ConfigurationPropertiesScan("ca.metricalsky.winston.config")
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
