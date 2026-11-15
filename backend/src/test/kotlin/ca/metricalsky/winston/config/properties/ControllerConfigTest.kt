package ca.metricalsky.winston.config.properties

import ca.metricalsky.winston.test.annotations.IntegrationTest
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe

@IntegrationTest
class ControllerConfigTest(
    controllerConfig: ControllerConfig,
) : WordSpec({

    "defaultSloMs" should {
        "return the configured default service level in milliseconds for controller methods" {
            controllerConfig.defaultSloMs shouldBe 100
        }
    }
})
