package ca.metricalsky.winston.client

import ca.metricalsky.winston.client.exception.CommentsDisabledException
import ca.metricalsky.winston.client.exception.YouTubeException
import ca.metricalsky.winston.config.YouTubeProvider
import ca.metricalsky.winston.config.properties.youtube.YouTubeConfig
import ca.metricalsky.winston.test.TestResources
import ca.metricalsky.winston.test.faker.WinstonFaker
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.http.Fault
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.util.DateTime
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.shouldBeTypeOf
import org.apache.http.HttpHeaders
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.wiremock.spring.EnableWireMock
import java.io.IOException

@SpringBootTest(classes = [YouTubeClient::class, YouTubeProvider::class])
@EnableConfigurationProperties(YouTubeConfig::class)
@EnableWireMock
class YouTubeClientTest(
    private val youTubeClient: YouTubeClient,
    private val youTubeConfig: YouTubeConfig,
) : WordSpec({

    val faker = WinstonFaker()
    val wireMock = YouTubeWireMock(youTubeConfig)

    beforeEach {
        wireMock.reset()
    }

    "getChannel()" should {

        val channelHandle = faker.youtube().channel().handle()
        val testResources = TestResources.dir("client", "channels")

        "handle a 200 response with channels" {
            wireMock.stubForGetChannels(channelHandle)
                .willReturn(okJson(testResources.load("200.json")))

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
            wireMock.stubForGetChannels(channelHandle)
                .willReturn(okJson(testResources.load("200_not_found.json")))

            youTubeClient.getChannel(channelHandle) shouldNotBeNull {
                items shouldBe null
            }
        }

        "throw YouTubeException on a faulty response" {
            wireMock.stubForGetChannels(channelHandle)
                .willReturn(aResponse().withFault(Fault.MALFORMED_RESPONSE_CHUNK))

            val exception = shouldThrow<YouTubeException> {
                youTubeClient.getChannel(channelHandle)
            }
            exception.status shouldBe HttpStatus.INTERNAL_SERVER_ERROR
            exception.cause.shouldBeTypeOf<IOException>()
        }
    }

    "getActivities()" should {

        val channelId = faker.youtube().channel().id()
        val testResources = TestResources.dir("client", "activities")

        "handle a 200 response with activities" {
            wireMock.stubForGetActivities(channelId)
                .willReturn(okJson(testResources.load("200.json")))

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
            wireMock.stubForGetActivities(channelId)
                .willReturn(okJson(testResources.load("200_empty.json")))

            youTubeClient.getActivities(channelId) shouldNotBeNull {
                items shouldBe emptyList()
            }
        }

        "throw YouTubeException on a faulty response" {
            wireMock.stubForGetActivities(channelId)
                .willReturn(aResponse().withFault(Fault.MALFORMED_RESPONSE_CHUNK))

            val exception = shouldThrow<YouTubeException> {
                youTubeClient.getActivities(channelId)
            }
            exception.status shouldBe HttpStatus.INTERNAL_SERVER_ERROR
            exception.cause.shouldBeTypeOf<IOException>()
        }
    }

    "getComments()" should {

        val videoId = faker.youtube().videoId()
        val testResources = TestResources.dir("client", "comments")

        "handle a 200 response with comments" {
            wireMock.stubForGetCommentThreads(videoId)
                .willReturn(okJson(testResources.load("200.json")))

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
            wireMock.stubForGetCommentThreads(videoId)
                .willReturn(okJson(testResources.load("200_empty.json")))

            youTubeClient.getComments(videoId) shouldNotBeNull {
                items shouldBe emptyList()
            }
        }

        "throw exception when comments are disabled" {
            wireMock.stubForGetCommentThreads(videoId).willReturn(forbidden()
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(testResources.load("403_comments_disabled.json")))

            val exception = shouldThrow<CommentsDisabledException> {
                youTubeClient.getComments(videoId)
            }
            exception.message shouldContain "Comments are disabled for the requested video."
            exception.status shouldBe HttpStatus.UNPROCESSABLE_ENTITY
            exception.cause.shouldBeTypeOf<GoogleJsonResponseException>()
        }

        "throw exception on a processing failure" {
            wireMock.stubForGetCommentThreads(videoId).willReturn(badRequest()
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(testResources.load("400_processing_failure.json")))

            val exception = shouldThrow<YouTubeException> {
                youTubeClient.getComments(videoId)
            }
            exception.status shouldBe HttpStatus.INTERNAL_SERVER_ERROR
            exception.cause.shouldBeTypeOf<GoogleJsonResponseException>()
        }

        "throw YouTubeException on a faulty response" {
            wireMock.stubForGetCommentThreads(videoId)
                .willReturn(aResponse().withFault(Fault.MALFORMED_RESPONSE_CHUNK))

            val exception = shouldThrow<YouTubeException> {
                youTubeClient.getComments(videoId)
            }
            exception.status shouldBe HttpStatus.INTERNAL_SERVER_ERROR
            exception.cause.shouldBeTypeOf<IOException>()
        }
    }

    "getReplies()" should {

        val commentId = faker.youtube().comment().id()
        val testResources = TestResources.dir("client", "replies")

        "handle a 200 response with replies" {
            wireMock.stubForGetComments(commentId)
                .willReturn(okJson(testResources.load("200.json")))

            youTubeClient.getReplies(commentId) shouldNotBeNull {
                items shouldHaveSize 1
                items[0].snippet shouldNotBeNull {
                    authorChannelId.value shouldBe "comment.snippet.authorChannelId.value"
                    authorChannelUrl shouldBe "https://www.example.com/comment/snippet/authorChannelUrl"
                    authorProfileImageUrl shouldBe "https://www.example.com/comment/snippet/authorProfileImageUrl"
                    authorDisplayName shouldBe "@commentAuthorDisplayName"
                    parentId shouldBe "comment.snippet.parentId"
                    publishedAt shouldBe DateTime("2025-01-01T00:00:00Z")
                    textDisplay shouldBe "comment.snippet.textDisplay"
                    textOriginal shouldBe "comment.snippet.textOriginal"
                    updatedAt shouldBe DateTime("2025-01-02T00:00:00Z")
                }
            }
        }

        "handle a 200 response with no replies" {
            wireMock.stubForGetComments(commentId)
                .willReturn(okJson(testResources.load("200_empty.json")))

            youTubeClient.getReplies(commentId) shouldNotBeNull {
                items shouldBe emptyList()
            }
        }

        "throw YouTubeException on a faulty response" {
            wireMock.stubForGetComments(commentId)
                .willReturn(aResponse().withFault(Fault.MALFORMED_RESPONSE_CHUNK))

            val exception = shouldThrow<YouTubeException> {
                youTubeClient.getReplies(commentId)
            }
            exception.status shouldBe HttpStatus.INTERNAL_SERVER_ERROR
            exception.cause.shouldBeTypeOf<IOException>()
        }
    }
})
