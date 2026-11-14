package ca.metricalsky.winston.service

import ca.metricalsky.winston.client.YouTubeClient
import ca.metricalsky.winston.client.YouTubeWireMock
import ca.metricalsky.winston.client.exception.ProcessingFailureException
import ca.metricalsky.winston.config.YouTubeProvider
import ca.metricalsky.winston.config.properties.YouTubeConfig
import ca.metricalsky.winston.database.entity.fetch.FetchActionEntity
import ca.metricalsky.winston.database.entity.fetch.YouTubeRequestEntity
import ca.metricalsky.winston.database.repository.fetch.YouTubeRequestRepository
import ca.metricalsky.winston.test.TestResources
import ca.metricalsky.winston.test.faker.WinstonFaker
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.marcinziolo.kotlin.wiremock.and
import com.marcinziolo.kotlin.wiremock.returnsJson
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeTypeOf
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.context.annotation.ComponentScan.Filter
import org.springframework.context.annotation.FilterType
import org.springframework.http.HttpStatus
import org.springframework.retry.annotation.EnableRetry
import org.wiremock.spring.EnableWireMock

@DataJpaTest(
    includeFilters = [Filter(
        type = FilterType.ASSIGNABLE_TYPE, classes = [
            YouTubeClient::class,
            YouTubeProvider::class,
            YouTubeRequestRepository::class,
            YouTubeService::class,
        ]
    )]
)
@EnableConfigurationProperties(YouTubeConfig::class)
@EnableWireMock
@EnableRetry
class YouTubeServiceTest(
    private val entityManager: TestEntityManager,
    private val youTubeConfig: YouTubeConfig,
    private val youTubeRequestRepository: YouTubeRequestRepository,
    private val youTubeService: YouTubeService,
) : WordSpec({

    val faker = WinstonFaker()
    val testResources = TestResources["client"]
    val wireMock = YouTubeWireMock(youTubeConfig)

    afterEach {
        wireMock.reset()
    }

    "getChannels()" should {

        var fetchAction: FetchActionEntity? = null

        beforeEach {
            val fetchRequest = entityManager.persist(faker.database().fetchRequest().minimalEntity())
            val fetchOperation = entityManager.persist(faker.database().fetchOperation().channels(fetchRequest!!))
            fetchAction = entityManager.persist(faker.database().fetchAction().channels(fetchOperation))
        }

        "handle a 200 response with channels" {
            wireMock.getChannelsFor(fetchAction!!.objectId) returnsJson {
                body = testResources["channels"].load("200.json")
            }

            youTubeService.getChannels(fetchAction) shouldNotBeNull {
                items shouldHaveSize 1
            }

            youTubeRequestRepository.findAllByFetchActionId(fetchAction.id!!) should {
                it shouldHaveSize 1
                it[0] shouldNotBeNull {
                    id shouldNotBe null
                    fetchActionId shouldBe fetchAction.id
                    requestType shouldBe YouTubeRequestEntity.RequestType.CHANNELS
                    objectId shouldBe fetchAction.objectId
                    requestedAt shouldNotBe null
                    httpStatus shouldBe HttpStatus.OK.value()
                    itemCount shouldBe 1
                    respondedAt shouldNotBe null
                }
            }
        }

        "handle a 200 response with no channels" {
            wireMock.getChannelsFor(fetchAction!!.objectId) returnsJson {
                body = testResources["channels"].load("200_not_found.json")
            }

            youTubeService.getChannels(fetchAction) shouldNotBeNull {
                items shouldBe null
            }

            youTubeRequestRepository.findAllByFetchActionId(fetchAction.id!!) should {
                it shouldHaveSize 1
                it[0] shouldNotBeNull {
                    id shouldNotBe null
                    fetchActionId shouldBe fetchAction.id
                    requestType shouldBe YouTubeRequestEntity.RequestType.CHANNELS
                    objectId shouldBe fetchAction.objectId
                    requestedAt shouldNotBe null
                    httpStatus shouldBe HttpStatus.OK.value()
                    itemCount shouldBe 0
                    respondedAt shouldNotBe null
                }
            }
        }
    }

    "getActivities()" should {

        var fetchAction: FetchActionEntity? = null

        beforeEach {
            val fetchRequest = entityManager.persist(faker.database().fetchRequest().minimalEntity())
            val fetchOperation = entityManager.persist(faker.database().fetchOperation().videos(fetchRequest!!))
            fetchAction = entityManager.persist(faker.database().fetchAction().videos(fetchOperation))
        }

        "handle a 200 response with activities" {
            wireMock.getActivitiesFor(fetchAction!!.objectId) returnsJson {
                body = testResources["activities"].load("200.json")
            }

            youTubeService.getActivities(fetchAction) shouldNotBeNull {
                items shouldHaveSize 2
            }

            youTubeRequestRepository.findAllByFetchActionId(fetchAction.id!!) should {
                it shouldHaveSize 1
                it[0] shouldNotBeNull {
                    id shouldNotBe null
                    fetchActionId shouldBe fetchAction.id
                    requestType shouldBe YouTubeRequestEntity.RequestType.ACTIVITIES
                    objectId shouldBe fetchAction.objectId
                    requestedAt shouldNotBe null
                    httpStatus shouldBe HttpStatus.OK.value()
                    itemCount shouldBe 2
                    respondedAt shouldNotBe null
                }
            }
        }

        "handle a 200 response with no activities" {
            wireMock.getActivitiesFor(fetchAction!!.objectId) returnsJson {
                body = testResources["activities"].load("200_empty.json")
            }

            youTubeService.getActivities(fetchAction) shouldNotBeNull {
                items shouldBe emptyList()
            }

            youTubeRequestRepository.findAllByFetchActionId(fetchAction.id!!) should {
                it shouldHaveSize 1
                it[0] shouldNotBeNull {
                    id shouldNotBe null
                    fetchActionId shouldBe fetchAction.id
                    requestType shouldBe YouTubeRequestEntity.RequestType.ACTIVITIES
                    objectId shouldBe fetchAction.objectId
                    requestedAt shouldNotBe null
                    httpStatus shouldBe HttpStatus.OK.value()
                    itemCount shouldBe 0
                    respondedAt shouldNotBe null
                }
            }
        }
    }

    "getComments()" should {

        var fetchAction: FetchActionEntity? = null

        beforeEach {
            val fetchRequest = entityManager.persist(faker.database().fetchRequest().minimalEntity())
            val fetchOperation = entityManager.persist(faker.database().fetchOperation().comments(fetchRequest!!))
            fetchAction = entityManager.persist(faker.database().fetchAction().comments(fetchOperation))
        }

        "handle a 200 response with comments" {
            wireMock.getCommentThreadsFor(fetchAction!!.objectId) returnsJson {
                body = testResources["comments"].load("200.json")
            }

            youTubeService.getComments(fetchAction) shouldNotBeNull {
                items shouldHaveSize 1
            }

            youTubeRequestRepository.findAllByFetchActionId(fetchAction.id!!) should {
                it shouldHaveSize 1
                it[0] shouldNotBeNull {
                    id shouldNotBe null
                    fetchActionId shouldBe fetchAction.id
                    requestType shouldBe YouTubeRequestEntity.RequestType.COMMENTS
                    objectId shouldBe fetchAction.objectId
                    requestedAt shouldNotBe null
                    httpStatus shouldBe HttpStatus.OK.value()
                    itemCount shouldBe 1
                    respondedAt shouldNotBe null
                }
            }
        }

        "handle a 200 response with no comments" {
            wireMock.getCommentThreadsFor(fetchAction!!.objectId) returnsJson {
                body = testResources["comments"].load("200_empty.json")
            }

            youTubeService.getComments(fetchAction) shouldNotBeNull {
                items shouldBe emptyList()
            }

            youTubeRequestRepository.findAllByFetchActionId(fetchAction.id!!) should {
                it shouldHaveSize 1
                it[0] shouldNotBeNull {
                    id shouldNotBe null
                    fetchActionId shouldBe fetchAction.id
                    requestType shouldBe YouTubeRequestEntity.RequestType.COMMENTS
                    objectId shouldBe fetchAction.objectId
                    requestedAt shouldNotBe null
                    httpStatus shouldBe HttpStatus.OK.value()
                    itemCount shouldBe 0
                    respondedAt shouldNotBe null
                }
            }
        }

        "retry the request on a processing failure" {
            wireMock.getCommentThreadsFor(fetchAction!!.objectId) returnsJson {
                statusCode = HttpStatus.BAD_REQUEST.value()
                body = testResources["comments"].load("400_processing_failure.json")
                toState = "recovered"
            }
            wireMock.getCommentThreadsFor(fetchAction.objectId) and {
                whenState = "recovered"
            } returnsJson {
                body = testResources["comments"].load("200.json")
            }

            youTubeService.getComments(fetchAction) shouldNotBeNull {
                items shouldHaveSize 1
            }

            youTubeRequestRepository.findAllByFetchActionId(fetchAction.id!!) should {
                it shouldHaveSize 2
                it[0].httpStatus shouldBe HttpStatus.INTERNAL_SERVER_ERROR.value()
                it[1].httpStatus shouldBe HttpStatus.OK.value()
            }
        }

        "throw exception on a processing failure after exhausting retries" {
            wireMock.getCommentThreadsFor(fetchAction!!.objectId) returnsJson {
                statusCode = HttpStatus.BAD_REQUEST.value()
                body = testResources["comments"].load("400_processing_failure.json")
            }

            val exception = shouldThrow<ProcessingFailureException> {
                youTubeService.getComments(fetchAction)
            }

            exception.status shouldBe HttpStatus.INTERNAL_SERVER_ERROR
            exception.cause.shouldBeTypeOf<GoogleJsonResponseException>()

            youTubeRequestRepository.findAllByFetchActionId(fetchAction.id!!) should {
                it shouldHaveSize 3
            }
        }
    }

    "getReplies()" should {

        var fetchAction: FetchActionEntity? = null

        beforeEach {
            val fetchRequest = entityManager.persist(faker.database().fetchRequest().minimalEntity())
            val fetchOperation = entityManager.persist(faker.database().fetchOperation().replies(fetchRequest!!))
            fetchAction = entityManager.persist(faker.database().fetchAction().replies(fetchOperation))
        }

        "handle a 200 response with replies" {
            wireMock.getCommentsFor(fetchAction!!.objectId) returnsJson {
                body = testResources["replies"].load("200.json")
            }

            youTubeService.getReplies(fetchAction) shouldNotBeNull {
                items shouldHaveSize 1
            }

            youTubeRequestRepository.findAllByFetchActionId(fetchAction.id!!) should {
                it shouldHaveSize 1
                it[0] shouldNotBeNull {
                    id shouldNotBe null
                    fetchActionId shouldBe fetchAction.id
                    requestType shouldBe YouTubeRequestEntity.RequestType.REPLIES
                    objectId shouldBe fetchAction.objectId
                    requestedAt shouldNotBe null
                    httpStatus shouldBe HttpStatus.OK.value()
                    itemCount shouldBe 1
                    respondedAt shouldNotBe null
                }
            }
        }

        "handle a 200 response with no replies" {
            wireMock.getCommentsFor(fetchAction!!.objectId) returnsJson {
                body = testResources["replies"].load("200_empty.json")
            }

            youTubeService.getReplies(fetchAction) shouldNotBeNull {
                items shouldBe emptyList()
            }

            youTubeRequestRepository.findAllByFetchActionId(fetchAction.id!!) should {
                it shouldHaveSize 1
                it[0] shouldNotBeNull {
                    id shouldNotBe null
                    fetchActionId shouldBe fetchAction.id
                    requestType shouldBe YouTubeRequestEntity.RequestType.REPLIES
                    objectId shouldBe fetchAction.objectId
                    requestedAt shouldNotBe null
                    httpStatus shouldBe HttpStatus.OK.value()
                    itemCount shouldBe 0
                    respondedAt shouldNotBe null
                }
            }
        }
    }
})
