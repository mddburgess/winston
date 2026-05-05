package ca.metricalsky.winston.database.entity

import ca.metricalsky.winston.database.test.annotation.DatabaseTest
import ca.metricalsky.winston.database.test.faker.DatabaseFaker
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DatabaseTest
class ThumbnailEntityTest(
    entityManager: TestEntityManager,
): WordSpec({

    val faker = DatabaseFaker()

    "ThumbnailEntity" should {

        "persist" {
            val thumbnail = faker.thumbnail().completeEntity()

            entityManager.persistFlushFind(thumbnail) shouldNotBeNull {
                this shouldNotBeSameInstanceAs thumbnail
                id shouldBe thumbnail.id
                url shouldBe thumbnail.url
                image shouldBe thumbnail.image
            }
        }
    }
})
