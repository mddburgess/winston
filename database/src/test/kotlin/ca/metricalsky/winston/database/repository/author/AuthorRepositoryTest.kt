package ca.metricalsky.winston.database.repository.author

import ca.metricalsky.winston.database.entity.author.AuthorEntity
import ca.metricalsky.winston.database.test.annotation.DatabaseTest
import ca.metricalsky.winston.database.test.faker.DatabaseFaker
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DatabaseTest
class AuthorRepositoryTest(
    authorRepository: AuthorRepository,
    entityManager: TestEntityManager,
): WordSpec({

    val faker = DatabaseFaker()

    var author: AuthorEntity? = null

    beforeEach {
        author = entityManager.persist(faker.author().completeEntity())
    }

    "findByChannelUrl()" should {

        "return an author when given its channel URL" {
            val result = authorRepository.findByChannelUrl(author?.channelUrl!!)
            result shouldBe author
        }

        "return null when given an empty channel URL" {
            val result = authorRepository.findByChannelUrl("")
            result shouldBe null
        }
    }

    "findByDisplayName()" should {

        "return an author when given its display name" {
            val result = authorRepository.findByDisplayName(author?.displayName!!)
            result shouldBe author
        }

        "return null when given an empty display name" {
            val result = authorRepository.findByDisplayName("")
            result shouldBe null
        }
    }
})
