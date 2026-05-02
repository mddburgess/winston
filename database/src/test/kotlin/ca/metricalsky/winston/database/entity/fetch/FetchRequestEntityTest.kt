package ca.metricalsky.winston.database.entity.fetch

import ca.metricalsky.winston.database.entity.fetch.FetchRequestEntity.Status.ACCEPTED
import ca.metricalsky.winston.database.test.annotation.DatabaseTest
import ca.metricalsky.winston.database.test.faker.DatabaseFaker
import ca.metricalsky.winston.database.test.jpa.ext.refreshAll
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DatabaseTest
class FetchRequestEntityTest(
    entityManager: TestEntityManager
): WordSpec({

    val faker = DatabaseFaker();

    "FetchRequestEntity" should {

        "persist with only required fields" {
            val fetchRequest = faker.fetchRequest().minimalEntity()

            entityManager.persistFlushFind(fetchRequest) shouldNotBeNull {
                id shouldNotBe null
                status shouldBe ACCEPTED
                operations shouldBe emptyList()
                createdAt shouldNotBe null
                lastUpdatedAt shouldNotBe null
            }
        }

        "persist operations" {
            val operation = FetchOperationEntity()
            operation.operationType = FetchOperationEntity.Type.CHANNELS
            operation.objectId = faker.channel().id()

            val fetchRequest = FetchRequestEntity(
                operations = mutableListOf(operation)
            )

            entityManager.persistFlushFind(fetchRequest) shouldNotBeNull {
                id shouldNotBe null

                entityManager.refreshAll(operations)
                operations shouldHaveSize fetchRequest.operations.size
                operations.forEach {
                    it.id shouldNotBe null
                    it.fetchRequestId shouldBe id
                }
            }
        }
    }
})
