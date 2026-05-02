package ca.metricalsky.winston.database.repository

import ca.metricalsky.winston.database.repository.author.AuthorJdbcRepository
import ca.metricalsky.winston.database.repository.author.AuthorRepository
import ca.metricalsky.winston.database.test.annotation.DatabaseTest
import ca.metricalsky.winston.database.test.faker.DatabaseFaker
import ca.metricalsky.winston.database.test.faker.ext.generateList
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe

@DatabaseTest
class AuthorJdbcRepositoryTest(
    authorJdbcRepository: AuthorJdbcRepository,
    authorRepository: AuthorRepository,
): WordSpec({

    val faker = DatabaseFaker()

    "saveAll()" should {

        "save a list of authors" {
            val authorsSequence = faker.collection({ faker.author().completeEntity() }).len(5)
            val authorsToInsert = authorsSequence.generateList()

            authorJdbcRepository.saveAll(authorsToInsert)
            authorRepository.findAll() shouldHaveSize 5

            val newAuthorsToInsert = authorsSequence.generateList()
            val authorsToInsertOrUpdate = authorsToInsert + newAuthorsToInsert

            authorJdbcRepository.saveAll(authorsToInsertOrUpdate)
            authorRepository.findAll() shouldHaveSize 10
        }

        "handle an empty list of authors" {
            authorJdbcRepository.saveAll(emptyList())
            authorRepository.findAll() shouldBe emptyList()
        }
    }
})
