package ca.metricalsky.winston.database.repository.comment

import ca.metricalsky.winston.database.test.annotation.DatabaseTest
import ca.metricalsky.winston.database.test.faker.DatabaseFaker
import ca.metricalsky.winston.database.test.faker.ext.generateList
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe

@DatabaseTest
class CommentJdbcRepositoryTest(
    commentJdbcRepository: CommentJdbcRepository,
    commentRepository: CommentRepository,
): WordSpec({

    val faker = DatabaseFaker()
    val author = faker.author().completeEntity()

    "saveAll()" should {

        "save a list of comments" {
            val commentsSequence = faker.collection({
                faker.comment().completeEntity(author = author)
            }).len(5)
            var commentsToInsert = commentsSequence.generateList()

            commentJdbcRepository.saveAll(commentsToInsert)
            commentRepository.findAll() shouldHaveSize 5

            val newCommentsToInsert = commentsSequence.generateList()
            val commentsToInsertOrUpdate = commentsToInsert + newCommentsToInsert

            commentJdbcRepository.saveAll(commentsToInsertOrUpdate)
            commentRepository.findAll() shouldHaveSize 10
        }

        "handle an empty list of comments" {
            commentJdbcRepository.saveAll(emptyList())
            commentRepository.findAll() shouldBe emptyList()
        }
    }
})
