package ca.metricalsky.winston.client

import ca.metricalsky.winston.client.exception.CommentsDisabledException
import ca.metricalsky.winston.client.exception.ProcessingFailureException
import ca.metricalsky.winston.config.YouTubeProvider
import ca.metricalsky.winston.config.properties.YouTubeConfig
import ca.metricalsky.winston.test.TestResources
import ca.metricalsky.winston.test.faker.WinstonFaker
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.util.DateTime
import com.marcinziolo.kotlin.wiremock.and
import com.marcinziolo.kotlin.wiremock.returnsJson
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.shouldBeTypeOf
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.retry.annotation.EnableRetry
import org.wiremock.spring.EnableWireMock

@SpringBootTest(classes = [YouTubeClient::class, YouTubeProvider::class])
@EnableConfigurationProperties(YouTubeConfig::class)
@EnableWireMock
@EnableRetry
class YouTubeClientTest(
    private val youTubeClient: YouTubeClient,
    private val youTubeConfig: YouTubeConfig,
) : WordSpec({

    val faker = WinstonFaker()
    val testResources = TestResources["client"]
    val wireMock = YouTubeWireMock(youTubeConfig)

    afterEach {
        wireMock.reset()
    }

    "getChannel()" should {

        val channelHandle = faker.youtube().channel().handle()

        "handle a 200 response with channels" {
            wireMock.getChannelsFor(channelHandle) returnsJson {
                body = testResources["channels"].load("200.json")
            }

            youTubeClient.getChannel(channelHandle) shouldNotBeNull {
                items shouldHaveSize 1
                items[0] shouldNotBeNull {
                    brandingSettings.channel.keywords shouldBe "channel \"branding settings\" keywords"
                    snippet shouldNotBeNull {
                        customUrl shouldBe "@channelCustomUrl"
                        description shouldBe "channel.snippet.description"
                        publishedAt shouldBe DateTime("2025-01-01T00:00:00Z")
                        thumbnails.high.url shouldBe "https://www.example.com/channel/snippet/thumbnails/high"
                        title shouldBe "channel.snippet.title"
                    }
                    topicDetails.topicCategories shouldContainExactly listOf(
                        "https://en.wikipedia.org/wiki/Topic", "https://en.wikipedia.org/wiki/Category"
                    )
                }
            }
        }

        "handle a 200 response with no channels" {
            wireMock.getChannelsFor(channelHandle) returnsJson {
                body = testResources["channels"].load("200_not_found.json")
            }

            youTubeClient.getChannel(channelHandle) shouldNotBeNull {
                items shouldBe null
            }
        }
    }

    "getActivities()" should {

        val channelId = faker.youtube().channel().id()

        "handle a 200 response with activities" {
            wireMock.getActivitiesFor(channelId) returnsJson {
                body = testResources["activities"].load("200.json")
            }

            youTubeClient.getActivities(channelId) shouldNotBeNull {
                items shouldHaveSize 2
                items[0] shouldNotBeNull {
                    contentDetails.upload.videoId shouldBe "videoActivity.contentDetails.upload.videoId"
                    snippet shouldNotBeNull {
                        this.channelId shouldBe "videoActivity.snippet.channelId"
                        description shouldBe "videoActivity.snippet.description"
                        publishedAt shouldBe DateTime("2025-01-01T00:00:00Z")
                        thumbnails.high.url shouldBe
                                "https://www.example.com/videoActivity/snippet/thumbnails/high"
                        title shouldBe "videoActivity.snippet.title"
                    }
                }
                items[1] shouldNotBeNull {
                    contentDetails.playlistItem.playlistId shouldBe
                            "playlistActivity.contentDetails.playlistItem.playlistId"
                    snippet shouldNotBeNull {
                        this.channelId shouldBe "playlistActivity.snippet.channelId"
                        description shouldBe "playlistActivity.snippet.description"
                        publishedAt shouldBe DateTime("2025-01-01T00:00:00Z")
                        thumbnails.high.url shouldBe
                                "https://www.example.com/playlistActivity/snippet/thumbnails/high"
                        title shouldBe "playlistActivity.snippet.title"
                    }
                }
            }
        }

        "handle a 200 response with no activities" {
            wireMock.getActivitiesFor(channelId) returnsJson {
                body = testResources["activities"].load("200_empty.json")
            }

            youTubeClient.getActivities(channelId) shouldNotBeNull {
                items shouldBe emptyList()
            }
        }
    }

    "getComments()" should {

        val videoId = faker.youtube().videoId()

        "handle a 200 response with comments" {
            wireMock.getCommentThreadsFor(videoId) returnsJson {
                body = testResources["comments"].load("200.json")
            }

            youTubeClient.getComments(videoId) shouldNotBeNull {
                items shouldHaveSize 1
                items[0].snippet.topLevelComment.snippet shouldNotBeNull {
                    authorChannelId.value shouldBe "topLevelComment.snippet.authorChannelId.value"
                    authorChannelUrl shouldBe "https://www.example.com/topLevelComment/snippet/authorChannelUrl"
                    authorProfileImageUrl shouldBe "https://www.example.com/topLevelComment/snippet/authorProfileImageUrl"
                    authorDisplayName shouldBe "@topLevelCommentAuthorDisplayName"
                    publishedAt shouldBe DateTime("2025-01-01T00:00:00Z")
                    textDisplay shouldBe "topLevelComment.snippet.textDisplay"
                    textOriginal shouldBe "topLevelComment.snippet.textOriginal"
                    updatedAt shouldBe DateTime("2025-01-02T00:00:00Z")
                    this.videoId shouldBe "topLevelComment.snippet.videoId"
                }
            }
        }

        "handle a 200 response with no comments" {
            wireMock.getCommentThreadsFor(videoId) returnsJson {
                body = testResources["comments"].load("200_empty.json")
            }

            youTubeClient.getComments(videoId) shouldNotBeNull {
                items shouldBe emptyList()
            }
        }

        "throw exception when comments are disabled" {
            wireMock.getCommentThreadsFor(videoId) returnsJson {
                statusCode = HttpStatus.FORBIDDEN.value()
                body = testResources["comments"].load("403_comments_disabled.json")
            }

            val exception = shouldThrow<CommentsDisabledException> {
                youTubeClient.getComments(videoId)
            }
            exception.message shouldContain "Comments are disabled for the requested video."
            exception.status shouldBe HttpStatus.UNPROCESSABLE_ENTITY
            exception.cause.shouldBeTypeOf<GoogleJsonResponseException>()
        }

        "retry the request on a processing failure" {
            wireMock.getCommentThreadsFor(videoId) returnsJson {
                statusCode = HttpStatus.BAD_REQUEST.value()
                body = testResources["comments"].load("400_processing_failure.json")
                toState = "recovered"
            }
            wireMock.getCommentThreadsFor(videoId) and {
                whenState = "recovered"
            } returnsJson {
                body = testResources["comments"].load("200.json")
            }

            youTubeClient.getComments(videoId) shouldNotBeNull {
                items.shouldNotBeEmpty()
            }
        }

        "throw exception on a processing failure" {
            wireMock.getCommentThreadsFor(videoId) returnsJson {
                statusCode = HttpStatus.BAD_REQUEST.value()
                body = testResources["comments"].load("400_processing_failure.json")
            }

            val exception = shouldThrow<ProcessingFailureException> {
                youTubeClient.getComments(videoId)
            }
            exception.status shouldBe HttpStatus.INTERNAL_SERVER_ERROR
            exception.cause.shouldBeTypeOf<GoogleJsonResponseException>()
        }
    }

    "getReplies()" should {

        val parentId = faker.youtube().comment().id()

        "handle a 200 response with replies" {
            wireMock.getCommentsFor(parentId) returnsJson {
                body = testResources["replies"].load("200.json")
            }

            youTubeClient.getReplies(parentId) shouldNotBeNull {
                items shouldHaveSize 1
                items[0].snippet shouldNotBeNull {
                    authorChannelId.value shouldBe "comment.snippet.authorChannelId.value"
                    authorChannelUrl shouldBe "https://www.example.com/comment/snippet/authorChannelUrl"
                    authorProfileImageUrl shouldBe "https://www.example.com/comment/snippet/authorProfileImageUrl"
                    authorDisplayName shouldBe "@commentAuthorDisplayName"
                    this.parentId shouldBe "comment.snippet.parentId"
                    publishedAt shouldBe DateTime("2025-01-01T00:00:00Z")
                    textDisplay shouldBe "comment.snippet.textDisplay"
                    textOriginal shouldBe "comment.snippet.textOriginal"
                    updatedAt shouldBe DateTime("2025-01-02T00:00:00Z")
                }
            }
        }

        "handle a 200 response with no replies" {
            wireMock.getCommentsFor(parentId) returnsJson {
                body = testResources["replies"].load("200_empty.json")
            }

            youTubeClient.getReplies(parentId) shouldNotBeNull {
                items shouldBe emptyList()
            }
        }
    }
})
