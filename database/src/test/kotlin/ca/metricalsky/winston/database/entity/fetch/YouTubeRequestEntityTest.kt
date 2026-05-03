package ca.metricalsky.winston.database.entity.fetch

import ca.metricalsky.winston.database.test.annotation.DatabaseTest
import ca.metricalsky.winston.database.test.faker.DatabaseFaker
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DatabaseTest
class YouTubeRequestEntityTest(
    entityManager: TestEntityManager,
) : WordSpec({

    val faker = DatabaseFaker()
    var fetchAction: FetchActionEntity? = null

    beforeEach {
        val fetchRequest = entityManager.persist(faker.fetchRequest().minimalEntity())
        val fetchOperation = entityManager.persist(faker.fetchOperation().minimalEntity(fetchRequest))
        fetchAction = entityManager.persist(faker.fetchAction().minimalEntity(fetchOperation))
    }

    "YouTubeRequestEntity" should {

        "persist with only required fields" {
            val youtubeRequest = faker.youtubeRequest().minimalEntity(fetchAction)

            entityManager.persistFlushFind(youtubeRequest) shouldNotBeNull {
                id shouldNotBe null
                fetchActionId shouldBe youtubeRequest.fetchActionId
                requestType shouldBe youtubeRequest.requestType
                objectId shouldBe youtubeRequest.objectId
                requestedAt shouldNotBe null
            }
        }
    }
})
