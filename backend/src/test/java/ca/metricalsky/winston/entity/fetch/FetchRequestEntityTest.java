package ca.metricalsky.winston.entity.fetch;

import ca.metricalsky.winston.entity.fetch.FetchOperationEntity.Type;
import ca.metricalsky.winston.entity.fetch.FetchRequestEntity.Status;
import ca.metricalsky.winston.test.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

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

    @Test
    void persistsOperations() {
        var fetchOperationEntity = FetchOperationEntity.builder()
                .operationType(Type.CHANNELS)
                .objectId(TestUtils.randomId())
                .build();
        var fetchRequestEntity = FetchRequestEntity.builder()
                .operations(List.of(fetchOperationEntity))
                .build();

        var persistedEntity = entityManager.persistFlushFind(fetchRequestEntity);

        assertThat(persistedEntity.getOperations())
                .hasSize(1);

        var persistedOperation = entityManager.refresh(persistedEntity.getOperations().getFirst());

        assertThat(persistedOperation.getId())
                .isNotNull();
        assertThat(persistedOperation.getFetchRequestId())
                .isNotNull()
                .isEqualTo(persistedEntity.getId());
    }
}
