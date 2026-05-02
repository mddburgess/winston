package ca.metricalsky.winston.database.entity.fetch

import ca.metricalsky.winston.database.entity.fetch.FetchOperationEntity.Status.READY
import ca.metricalsky.winston.database.test.annotation.DatabaseTest
import ca.metricalsky.winston.database.test.faker.DatabaseFaker
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DatabaseTest
class FetchOperationEntityTest(
    entityManager: TestEntityManager,
): WordSpec({

    val faker = DatabaseFaker();

    "FetchOperationEntity" should {

        "persist with only required fields" {
            val fetchOperation = faker.fetchOperation().minimalEntity()

            entityManager.persistFlushFind(fetchOperation) shouldNotBeNull {
                id shouldNotBe null
                operationType shouldBe fetchOperation.operationType
                objectId shouldBe fetchOperation.objectId
                status shouldBe READY
                createdAt shouldNotBe null
                lastUpdatedAt shouldNotBe null
            }
        }

        "persist with all optional fields" {
            val fetchRequest = entityManager.persist(faker.fetchRequest().minimalEntity())
            val fetchOperation = faker.fetchOperation().completeEntity(fetchRequest)

            entityManager.persistFlushFind(fetchOperation) shouldNotBeNull {
                id shouldNotBe null
                fetchRequestId shouldBe fetchRequest.id
                operationType shouldBe fetchOperation.operationType
                objectId shouldBe fetchOperation.objectId
                mode shouldBe fetchOperation.mode
                publishedAfter shouldBe fetchOperation.publishedAfter
                publishedBefore shouldBe fetchOperation.publishedBefore
                status shouldBe fetchOperation.status
                createdAt shouldNotBe null
                lastUpdatedAt shouldNotBe null
            }
        }
    }
})
