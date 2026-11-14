package ca.metricalsky.winston

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.extensions.spring.SpringExtension

class TestConfig: AbstractProjectConfig() {

    override val extensions = listOf(SpringExtension())
}
