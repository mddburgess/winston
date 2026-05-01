package ca.metricalsky.winston.database.repository

import ca.metricalsky.winston.database.ext.generateList
import ca.metricalsky.winston.database.repository.author.AuthorJdbcRepository
import ca.metricalsky.winston.database.repository.author.AuthorRepository
import ca.metricalsky.winston.database.test.annotation.DatabaseTest
import ca.metricalsky.winston.database.test.faker.DatabaseFaker
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe

@DatabaseTest
class AuthorJdbcRepositoryTest(
    authorJdbcRepository: AuthorJdbcRepository,
    authorRepository: AuthorRepository,
): StringSpec({

    val faker = DatabaseFaker()

    "saveAll() saves a list of authors" {
        val authorsSequence = faker.collection({ faker.author().complete() }).len(5)
        val authorsToInsert = authorsSequence.generateList()

        authorJdbcRepository.saveAll(authorsToInsert)
        authorRepository.findAll() shouldHaveSize 5

        val newAuthorsToInsert = authorsSequence.generateList()
        val authorsToInsertOrUpdate = authorsToInsert + newAuthorsToInsert

        authorJdbcRepository.saveAll(authorsToInsertOrUpdate)
        authorRepository.findAll() shouldHaveSize 10
    }

    "saveAll() handles an empty list of authors" {
        authorJdbcRepository.saveAll(emptyList())
        authorRepository.findAll() shouldBe emptyList()
    }
})
