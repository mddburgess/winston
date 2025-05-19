package ca.metricalsky.winston.entity.fetch;

import ca.metricalsky.winston.test.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class FetchActionEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    private FetchOperationEntity fetchOperationEntity;

    @BeforeEach
    void beforeEach() {
        var fetchRequestEntity = persistFetchRequest();
        fetchOperationEntity = persistFetchOperation(fetchRequestEntity);
    }

    @Test
    void persistsWithOnlyRequiredFields() {
        var fetchActionEntity = FetchActionEntity.builder()
                .fetchOperationId(fetchOperationEntity.getId())
                .actionType(FetchActionEntity.Type.CHANNELS)
                .objectId(TestUtils.randomId())
                .build();

        var persistedEntity = entityManager.persistFlushFind(fetchActionEntity);

        assertThat(persistedEntity)
                .hasFieldOrPropertyWithValue("fetchOperationId", fetchActionEntity.getFetchOperationId())
                .hasFieldOrPropertyWithValue("actionType", fetchActionEntity.getActionType())
                .hasFieldOrPropertyWithValue("objectId", fetchActionEntity.getObjectId())
                .hasFieldOrPropertyWithValue("status", FetchActionEntity.Status.READY);
        assertThat(persistedEntity.getId())
                .isNotNull();
        assertThat(persistedEntity.getCreatedAt())
                .isNotNull();
        assertThat(persistedEntity.getLastUpdatedAt())
                .isNotNull();
    }

    @Test
    void persistsWithAllOptionalFields() {
        var fetchActionEntity = FetchActionEntity.builder()
                .fetchOperationId(fetchOperationEntity.getId())
                .actionType(FetchActionEntity.Type.CHANNELS)
                .objectId(TestUtils.randomId())
                .publishedAfter(OffsetDateTime.now())
                .publishedBefore(OffsetDateTime.now())
                .pageToken(TestUtils.randomId())
                .status(FetchActionEntity.Status.FAILED)
                .itemCount(TestUtils.randomInt())
                .error(TestUtils.randomString())
                .build();

        var persistedEntity = entityManager.persistFlushFind(fetchActionEntity);

        assertThat(persistedEntity)
                .hasFieldOrPropertyWithValue("fetchOperationId", fetchActionEntity.getFetchOperationId())
                .hasFieldOrPropertyWithValue("actionType", fetchActionEntity.getActionType())
                .hasFieldOrPropertyWithValue("objectId", fetchActionEntity.getObjectId())
                .hasFieldOrPropertyWithValue("publishedAfter",
                        fetchActionEntity.getPublishedAfter().withOffsetSameInstant(ZoneOffset.UTC))
                .hasFieldOrPropertyWithValue("publishedBefore",
                        fetchActionEntity.getPublishedBefore().withOffsetSameInstant(ZoneOffset.UTC))
                .hasFieldOrPropertyWithValue("pageToken", fetchActionEntity.getPageToken())
                .hasFieldOrPropertyWithValue("status", fetchActionEntity.getStatus())
                .hasFieldOrPropertyWithValue("itemCount", fetchActionEntity.getItemCount())
                .hasFieldOrPropertyWithValue("error", fetchActionEntity.getError())
                .hasNoNullFieldsOrProperties();

    }

    private FetchRequestEntity persistFetchRequest() {
        var fetchRequestEntity = FetchRequestEntity.builder()
                .build();
        return entityManager.persist(fetchRequestEntity);
    }

    private FetchOperationEntity persistFetchOperation(FetchRequestEntity fetchRequestEntity) {
        var fetchOperationEntity = FetchOperationEntity.builder()
                .fetchRequestId(fetchRequestEntity.getId())
                .operationType(FetchOperationEntity.Type.CHANNELS)
                .objectId(TestUtils.randomId())
                .build();
        return entityManager.persist(fetchOperationEntity);
    }
}
