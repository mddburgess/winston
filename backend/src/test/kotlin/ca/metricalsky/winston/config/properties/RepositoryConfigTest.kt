package ca.metricalsky.winston.config.properties

import ca.metricalsky.winston.test.annotations.IntegrationTest
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe

@IntegrationTest
class RepositoryConfigTest(
    repositoryConfig: RepositoryConfig,
) : WordSpec({

    "defaultSloMs" should {
        "return the configured default service level in milliseconds for repository methods" {
            repositoryConfig.defaultSloMs shouldBe 5
        }
    }
})
