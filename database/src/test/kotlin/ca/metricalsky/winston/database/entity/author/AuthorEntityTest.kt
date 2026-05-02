package ca.metricalsky.winston.database.entity.author

import ca.metricalsky.winston.database.test.annotation.DatabaseTest
import ca.metricalsky.winston.database.test.faker.DatabaseFaker
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.date.shouldHaveSameInstantAs
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DatabaseTest
class AuthorEntityTest(
    entityManager: TestEntityManager,
): WordSpec({

    val faker = DatabaseFaker()

    "AuthorEntity" should {

        "persist with only required fields" {
            val author = faker.author().minimalEntity()

            entityManager.persistFlushFind(author) shouldNotBeNull {
                id shouldBe author.id
                displayName shouldBe null
                channelUrl shouldBe null
                profileImageUrl shouldBe null
                lastFetchedAt shouldNotBe null
                aliases shouldBe emptySet()
            }
        }

        "persist with all optional fields" {
            val author = faker.author().completeEntity()

            entityManager.persistFlushFind(author) shouldNotBeNull {
                id shouldBe author.id
                displayName shouldBe author.displayName
                channelUrl shouldBe author.channelUrl
                profileImageUrl shouldBe author.profileImageUrl
                lastFetchedAt shouldHaveSameInstantAs author.lastFetchedAt
                aliases shouldContainExactly author.aliases
            }
        }
    }
})
