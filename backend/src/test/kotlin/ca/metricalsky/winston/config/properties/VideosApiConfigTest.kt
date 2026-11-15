package ca.metricalsky.winston.config.properties

import ca.metricalsky.winston.test.annotations.IntegrationTest
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe

@IntegrationTest
class VideosApiConfigTest(
    videosApiConfig: VideosApiConfig,
) : WordSpec({

    "defaultPageSize" should {
        "return the configured default page size for the list videos response" {
            videosApiConfig.defaultPageSize shouldBe 10
        }
    }
})
