package ca.metricalsky.winston.database.entity

import ca.metricalsky.winston.database.test.faker.DatabaseFaker
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@DataJpaTest
class AuthorEntityTest {

    val faker = DatabaseFaker()

    @Autowired
    lateinit var entityManager: TestEntityManager

    @Test
    fun `persists with only required fields`() {
        val author = faker.author().minimal()

        val persisted = entityManager.persistFlushFind(author);

        assertEquals(author.id, persisted.id)
        assertNull(persisted.displayName)
        assertNull(persisted.channelUrl)
        assertNull(persisted.profileImageUrl)
        assertNotNull(persisted.lastFetchedAt)
        assertEquals(setOf(), persisted.aliases)
    }

    @Test
    fun `persists with all optional fields`() {
        val author = faker.author().complete()

        val persisted = entityManager.persistFlushFind(author);

        assertEquals(author.id, persisted.id)
        assertEquals(author.displayName, persisted.displayName)
        assertEquals(author.channelUrl, persisted.channelUrl)
        assertEquals(author.profileImageUrl, persisted.profileImageUrl)
        assertNotNull(persisted.lastFetchedAt)
        assertEquals(author.aliases, persisted.aliases)
    }
}
