package ca.metricalsky.winston.entity.fetch;

import ca.metricalsky.winston.entity.fetch.YouTubeRequestEntity.RequestType;
import ca.metricalsky.winston.test.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class YouTubeRequestEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    private FetchActionEntity fetchActionEntity;

    @BeforeEach
    void beforeEach() {
        var fetchRequestEntity = persistFetchRequest();
        var fetchOperationEntity = persistFetchOperation(fetchRequestEntity);
        fetchActionEntity = persistFetchAction(fetchOperationEntity);
    }

    @Test
    void persistsWithOnlyRequiredFields() {
        var youtubeRequestEntity = YouTubeRequestEntity.builder()
                .fetchActionId(fetchActionEntity.getId())
                .requestType(RequestType.CHANNELS)
                .objectId(TestUtils.randomId())
                .requestedAt(OffsetDateTime.now())
                .build();

        var persistedEntity = entityManager.persistFlushFind(youtubeRequestEntity);

        assertThat(persistedEntity)
                .hasFieldOrPropertyWithValue("fetchActionId", youtubeRequestEntity.getFetchActionId())
                .hasFieldOrPropertyWithValue("requestType", youtubeRequestEntity.getRequestType())
                .hasFieldOrPropertyWithValue("objectId", youtubeRequestEntity.getObjectId());
        assertThat(persistedEntity.getId())
                .isNotNull();
        assertThat(persistedEntity.getRequestedAt())
                .isNotNull();
    }

    @Test
    void persistsWithAllOptionalFields() {
        var youtubeRequestEntity = YouTubeRequestEntity.builder()
                .fetchActionId(fetchActionEntity.getId())
                .requestType(RequestType.CHANNELS)
                .objectId(TestUtils.randomId())
                .publishedAfter(OffsetDateTime.now().toString())
                .publishedBefore(OffsetDateTime.now().toString())
                .pageToken(TestUtils.randomId())
                .requestedAt(OffsetDateTime.now())
                .httpStatus(TestUtils.randomInt())
                .itemCount(TestUtils.randomInt())
                .error(TestUtils.randomString())
                .respondedAt(OffsetDateTime.now())
                .build();

        var persistedEntity = entityManager.persistFlushFind(youtubeRequestEntity);

        assertThat(persistedEntity)
                .hasFieldOrPropertyWithValue("fetchActionId", youtubeRequestEntity.getFetchActionId())
                .hasFieldOrPropertyWithValue("requestType", youtubeRequestEntity.getRequestType())
                .hasFieldOrPropertyWithValue("objectId", youtubeRequestEntity.getObjectId())
                .hasFieldOrPropertyWithValue("publishedAfter", youtubeRequestEntity.getPublishedAfter())
                .hasFieldOrPropertyWithValue("publishedBefore", youtubeRequestEntity.getPublishedBefore())
                .hasFieldOrPropertyWithValue("pageToken", youtubeRequestEntity.getPageToken())
                .hasFieldOrPropertyWithValue("httpStatus", youtubeRequestEntity.getHttpStatus())
                .hasFieldOrPropertyWithValue("itemCount", youtubeRequestEntity.getItemCount())
                .hasFieldOrPropertyWithValue("error", youtubeRequestEntity.getError())
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

    private FetchActionEntity persistFetchAction(FetchOperationEntity fetchOperationEntity) {
        var fetchActionEntity = FetchActionEntity.builder()
                .fetchOperationId(fetchOperationEntity.getId())
                .actionType(FetchActionEntity.Type.CHANNELS)
                .objectId(TestUtils.randomId())
                .build();
        return entityManager.persist(fetchActionEntity);
    }
}
