//package ca.metricalsky.winston.repository;
//
//
//import ca.metricalsky.winston.database.entity.author.AuthorEntity;
//import ca.metricalsky.winston.database.repository.author.AuthorRepository;
//import ca.metricalsky.winston.database.repository.author.AuthorJdbcRepository;
//import ca.metricalsky.winston.test.annotations.RepositoryTest;
//import ca.metricalsky.winston.test.TestUtils;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.time.OffsetDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Set;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@RepositoryTest
//class AuthorJdbcRepositoryTest {
//
//    @Autowired
//    private AuthorJdbcRepository authorJdbcRepository;
//    @Autowired
//    private AuthorRepository authorRepository;
//
//    @Test
//    void saveAll() {
//        var authorsToInsert = new ArrayList<AuthorEntity>();
//        for (int i = 0; i < 5; ++i) {
//            authorsToInsert.add(createAuthorEntity());
//        }
//
//        authorJdbcRepository.saveAll(authorsToInsert);
//
//        assertThat(authorRepository.findAll()).hasSize(5);
//
//        var authorsToInsertOrUpdate = new ArrayList<AuthorEntity>();
//        for (var author: authorsToInsert) {
//            authorsToInsertOrUpdate.add(copyAuthorEntity(author));
//        }
//        for (int i = 0; i < 5; ++i) {
//            authorsToInsertOrUpdate.add(createAuthorEntity());
//        }
//
//        authorJdbcRepository.saveAll(authorsToInsertOrUpdate);
//
//        assertThat(authorRepository.findAll()).hasSize(10);
//    }
//
//    @Test
//    void saveAllEmptyList() {
//        authorJdbcRepository.saveAll(List.of());
//
//        assertThat(authorRepository.findAll()).isEmpty();
//    }
//
//    private static AuthorEntity createAuthorEntity() {
//        return new AuthorEntity(
//                TestUtils.randomId(),
//                TestUtils.randomString(),
//                TestUtils.randomString(),
//                TestUtils.randomString(),
//                OffsetDateTime.now(),
//                Set.of()
//        );
//    }
//
//    private static AuthorEntity copyAuthorEntity(AuthorEntity authorEntity) {
//        return new AuthorEntity(
//                authorEntity.getId(),
//                authorEntity.getDisplayName(),
//                authorEntity.getChannelUrl(),
//                authorEntity.getProfileImageUrl(),
//                authorEntity.getLastFetchedAt(),
//                authorEntity.getAliases()
//        );
//    }
//}
