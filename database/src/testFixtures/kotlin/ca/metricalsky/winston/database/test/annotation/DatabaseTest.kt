package ca.metricalsky.winston.database.test.annotation

import io.kotest.core.extensions.ApplyExtension
import io.kotest.extensions.spring.SpringExtension
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.ComponentScan

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@ApplyExtension(SpringExtension::class)
@DataJpaTest
@ComponentScan(basePackages = ["ca.metricalsky.winston.database.repository"])
annotation class DatabaseTest
