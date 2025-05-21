package ca.metricalsky.winston.entity;

import ca.metricalsky.winston.test.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ThumbnailEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void persists() {
        var thumbnailEntity = ThumbnailEntity.builder()
                .id(TestUtils.randomId())
                .url(TestUtils.randomString())
                .image(TestUtils.randomBytes(1000))
                .build();

        var persistedEntity = entityManager.persistFlushFind(thumbnailEntity);

        assertThat(persistedEntity)
                .hasFieldOrPropertyWithValue("id", thumbnailEntity.getId())
                .hasFieldOrPropertyWithValue("url", thumbnailEntity.getUrl())
                .hasFieldOrPropertyWithValue("image", thumbnailEntity.getImage())
                .hasNoNullFieldsOrProperties();
    }
}
