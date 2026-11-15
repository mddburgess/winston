package ca.metricalsky.winston.config.properties

import ca.metricalsky.winston.test.annotations.IntegrationTest
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.equals.shouldEqual
import io.kotest.matchers.string.shouldMatch

@IntegrationTest
class YouTubeConfigTest(
    youTubeConfig: YouTubeConfig,
) : WordSpec({

    "apiKey" should {
        "return the configured API key" {
            youTubeConfig.apiKey shouldEqual "YOUTUBE_API_KEY"
        }
    }

    "dailyRequestQuota" should {
        "return the configured daily request quota" {
            youTubeConfig.dailyRequestQuota shouldEqual 10_000
        }
    }

    "mockRootUrl" should {
        "return the configured mock root URL" {
            youTubeConfig.mockRootUrl shouldMatch Regex("http://localhost:\\d+")
        }
    }
})
