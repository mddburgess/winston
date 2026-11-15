package ca.metricalsky.winston.config.properties

import ca.metricalsky.winston.test.annotations.IntegrationTest
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe

@IntegrationTest
class AuthorsApiConfigTest(
    authorsApiConfig: AuthorsApiConfig,
) : WordSpec({

    "defaultPageSize" should {
        "return the configured default page size for the list authors response" {
            authorsApiConfig.defaultPageSize shouldBe 10
        }
    }
})
