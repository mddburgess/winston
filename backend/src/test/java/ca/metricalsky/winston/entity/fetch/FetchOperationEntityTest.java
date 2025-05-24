package ca.metricalsky.winston.entity.fetch;

import ca.metricalsky.winston.entity.fetch.FetchOperationEntity.Status;
import ca.metricalsky.winston.entity.fetch.FetchOperationEntity.Type;
import ca.metricalsky.winston.test.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class FetchOperationEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void persistsWithOnlyRequiredFields() {
        var fetchOperationEntity = FetchOperationEntity.builder()
                .operationType(Type.CHANNELS)
                .objectId(TestUtils.randomId())
                .build();

        var persistedEntity = entityManager.persistFlushFind(fetchOperationEntity);

        assertThat(persistedEntity)
                .hasFieldOrPropertyWithValue("operationType", fetchOperationEntity.getOperationType())
                .hasFieldOrPropertyWithValue("objectId", fetchOperationEntity.getObjectId())
                .hasFieldOrPropertyWithValue("status", Status.READY);
        assertThat(persistedEntity.getId())
                .isNotNull();
        assertThat(persistedEntity.getCreatedAt())
                .isNotNull();
        assertThat(persistedEntity.getLastUpdatedAt())
                .isNotNull();
    }

    @Test
    void persistsWithAllOptionalFields() {
        var fetchRequestEntity = persistFetchRequest();
        var fetchOperationEntity = FetchOperationEntity.builder()
                .fetchRequestId(fetchRequestEntity.getId())
                .operationType(Type.CHANNELS)
                .objectId(TestUtils.randomId())
                .mode(TestUtils.randomString())
                .publishedAfter(OffsetDateTime.now())
                .publishedBefore(OffsetDateTime.now())
                .status(Status.FAILED)
                .error(TestUtils.randomString())
                .build();

        var persistedEntity = entityManager.persistFlushFind(fetchOperationEntity);

        assertThat(persistedEntity)
                .hasFieldOrPropertyWithValue("fetchRequestId", fetchOperationEntity.getFetchRequestId())
                .hasFieldOrPropertyWithValue("operationType", fetchOperationEntity.getOperationType())
                .hasFieldOrPropertyWithValue("objectId", fetchOperationEntity.getObjectId())
                .hasFieldOrPropertyWithValue("mode", fetchOperationEntity.getMode())
                .hasFieldOrPropertyWithValue("status", fetchOperationEntity.getStatus())
                .hasFieldOrPropertyWithValue("error", fetchOperationEntity.getError())
                .hasNoNullFieldsOrProperties();
    }

    private FetchRequestEntity persistFetchRequest() {
        var fetchRequestEntity = FetchRequestEntity.builder()
                .build();
        return entityManager.persist(fetchRequestEntity);
    }
}
