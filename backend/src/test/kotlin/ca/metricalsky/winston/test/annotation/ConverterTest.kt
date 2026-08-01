package ca.metricalsky.winston.test.annotation

import io.kotest.core.extensions.ApplyExtension
import io.kotest.extensions.spring.SpringExtension
import org.mapstruct.extensions.spring.test.ConverterScan

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@ApplyExtension(SpringExtension::class)
@ConverterScan(basePackages = ["ca.metricalsky.winston.convert"])
annotation class ConverterTest
