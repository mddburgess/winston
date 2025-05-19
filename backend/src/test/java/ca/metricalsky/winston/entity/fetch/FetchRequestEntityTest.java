package ca.metricalsky.winston.entity.fetch;

import ca.metricalsky.winston.entity.fetch.FetchRequestEntity.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class FetchRequestEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void persists() {
        var fetchRequestEntity = FetchRequestEntity.builder()
                .build();

        var persistedEntity = entityManager.persistFlushFind(fetchRequestEntity);

        assertThat(persistedEntity)
                .hasFieldOrPropertyWithValue("status", Status.ACCEPTED)
                .hasNoNullFieldsOrProperties();
        assertThat(persistedEntity.getOperations())
                .isEmpty();
    }
}
