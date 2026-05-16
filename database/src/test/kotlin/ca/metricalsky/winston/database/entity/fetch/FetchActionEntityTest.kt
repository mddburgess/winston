package ca.metricalsky.winston.database.entity.fetch

import ca.metricalsky.winston.database.entity.fetch.FetchActionEntity.Status.READY
import ca.metricalsky.winston.database.test.annotation.DatabaseTest
import ca.metricalsky.winston.database.test.faker.DatabaseFaker
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DatabaseTest
class FetchActionEntityTest(
    entityManager: TestEntityManager,
): WordSpec({

    val faker = DatabaseFaker()
    var fetchOperation: FetchOperationEntity? = null

    beforeEach {
        val fetchRequest = entityManager.persist(faker.fetchRequest().minimalEntity())
        fetchOperation = entityManager.persist(faker.fetchOperation().minimalEntity(fetchRequest))
    }

    "FetchActionEntity" should {

        "persist with only required fields" {
            val fetchAction = faker.fetchAction().minimalEntity(fetchOperation)

            entityManager.persistFlushFind(fetchAction) shouldNotBeNull {
                id shouldNotBe null
                fetchOperationId shouldBe fetchAction.fetchOperationId
                actionType shouldBe fetchAction.actionType
                objectId shouldBe fetchAction.objectId
                status shouldBe READY
                createdAt shouldNotBe null
                lastUpdatedAt shouldNotBe null
            }
        }

        "persist with all optional fields" {
            val fetchAction = faker.fetchAction().completeEntity(fetchOperation)

            entityManager.persistFlushFind(fetchAction) shouldNotBeNull {
                id shouldNotBe null
                fetchOperationId shouldBe fetchAction.fetchOperationId
                actionType shouldBe fetchAction.actionType
                objectId shouldBe fetchAction.objectId
                publishedAfter shouldNotBe null
                publishedBefore shouldNotBe null
                status shouldBe READY
                createdAt shouldNotBe null
                lastUpdatedAt shouldNotBe null
            }
        }
    }
})
