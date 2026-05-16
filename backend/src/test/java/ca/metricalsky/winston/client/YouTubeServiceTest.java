package ca.metricalsky.winston.client;

import ca.metricalsky.winston.config.YouTubeProvider;
import ca.metricalsky.winston.config.properties.youtube.YouTubeConfig;
import ca.metricalsky.winston.database.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.database.entity.fetch.FetchRequestEntity;
import ca.metricalsky.winston.database.entity.fetch.YouTubeRequestEntity;
import ca.metricalsky.winston.database.repository.fetch.YouTubeRequestRepository;
import ca.metricalsky.winston.service.YouTubeService;
import ca.metricalsky.winston.test.TestResources;
import ca.metricalsky.winston.test.faker.WinstonFaker;
import com.github.tomakehurst.wiremock.http.Fault;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactory;
import org.assertj.core.api.ObjectAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.wiremock.spring.EnableWireMock;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.forbidden;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest(includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
        YouTubeClient.class,
        YouTubeService.class,
        YouTubeProvider.class,
        YouTubeRequestRepository.class,
}))
@EnableConfigurationProperties(YouTubeConfig.class)
@EnableWireMock
class YouTubeServiceTest {

    private static final TestResources TEST_RESOURCES = TestResources.dir("client");
    private static final WinstonFaker faker = new WinstonFaker();

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private YouTubeConfig youTubeConfig;
    @Autowired
    private YouTubeService clientAdapter;
    @Autowired
    private YouTubeRequestRepository requestRepository;

    private YouTubeWireMock wireMock;
    private FetchRequestEntity fetchRequest;
    private FetchActionEntity fetchAction;

    @BeforeEach
    void beforeEach() {
        wireMock = new YouTubeWireMock(youTubeConfig.getApiKey());
        fetchRequest = entityManager.persist(faker.database().fetchRequest().minimalEntity());
    }

    @Nested
    class Channels {

        @BeforeEach
        void beforeEach() {
            var fetchOperation = entityManager.persist(faker.database().fetchOperation().channels(fetchRequest));
            fetchAction = entityManager.persist(faker.database().fetchAction().channels(fetchOperation));
        }

        @Test
        void getChannels() {
            wireMock.stubForGetChannels(fetchAction.getObjectId())
                    .willReturn(okJson(TEST_RESOURCES.load("channels", "200.json")));

            var result = clientAdapter.getChannels(fetchAction);

            assertThat(result).isNotNull();
            assertThat(result.getItems()).hasSize(1);
            assertThatYouTubeRequest(fetchAction)
                    .hasFieldOrPropertyWithValue("requestType", YouTubeRequestEntity.RequestType.CHANNELS)
                    .hasFieldOrPropertyWithValue("objectId", fetchAction.getObjectId())
                    .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.OK.value())
                    .hasFieldOrPropertyWithValue("itemCount", result.getItems().size());
        }

        @Test
        void getChannels_notFound() {
            wireMock.stubForGetChannels(fetchAction.getObjectId())
                    .willReturn(okJson(TEST_RESOURCES.load("channels", "200_not_found.json")));

            var result = clientAdapter.getChannels(fetchAction);

            assertThat(result).isNotNull();
            assertThat(result.getItems()).isNull();
            assertThatYouTubeRequest(fetchAction)
                    .hasFieldOrPropertyWithValue("requestType", YouTubeRequestEntity.RequestType.CHANNELS)
                    .hasFieldOrPropertyWithValue("objectId", fetchAction.getObjectId())
                    .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.OK.value())
                    .hasFieldOrPropertyWithValue("itemCount", 0);
        }

        @Test
        void getChannels_fault() {
            wireMock.stubForGetChannels(fetchAction.getObjectId())
                    .willReturn(aResponse().withFault(Fault.MALFORMED_RESPONSE_CHUNK));

            assertThatThrownBy(() -> clientAdapter.getChannels(fetchAction))
                    .isInstanceOf(YouTubeException.class)
                    .hasFieldOrPropertyWithValue("status", HttpStatus.INTERNAL_SERVER_ERROR)
                    .hasCauseExactlyInstanceOf(IOException.class);

            assertThatYouTubeRequest(fetchAction)
                    .hasFieldOrPropertyWithValue("requestType", YouTubeRequestEntity.RequestType.CHANNELS)
                    .hasFieldOrPropertyWithValue("objectId", fetchAction.getObjectId())
                    .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .hasFieldOrProperty("error");
        }
    }

    @Nested
    class Videos {

        @BeforeEach
        void beforeEach() {
            var fetchOperation = entityManager.persist(faker.database().fetchOperation().videos(fetchRequest));
            fetchAction = entityManager.persist(faker.database().fetchAction().videos(fetchOperation));
        }

        @ParameterizedTest
        @CsvSource({
                "200.json, 2",
                "200_empty.json, 0"
        })
        void getActivities(String testResource, int expectedItemCount) {
            wireMock.stubForGetActivities(fetchAction.getObjectId())
                    .willReturn(okJson(TEST_RESOURCES.load("activities", testResource)));

            var result = clientAdapter.getActivities(fetchAction);

            assertThat(result).isNotNull();
            assertThat(result.getItems()).hasSize(expectedItemCount);
            assertThatYouTubeRequest(fetchAction)
                    .hasFieldOrPropertyWithValue("requestType", YouTubeRequestEntity.RequestType.ACTIVITIES)
                    .hasFieldOrPropertyWithValue("objectId", fetchAction.getObjectId())
                    .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.OK.value())
                    .hasFieldOrPropertyWithValue("itemCount", expectedItemCount);
        }

        @Test
        void getActivities_fault() {
            wireMock.stubForGetActivities(fetchAction.getObjectId())
                    .willReturn(aResponse().withFault(Fault.MALFORMED_RESPONSE_CHUNK));

            assertThatThrownBy(() -> clientAdapter.getActivities(fetchAction))
                    .isInstanceOf(YouTubeException.class)
                    .hasFieldOrPropertyWithValue("status", HttpStatus.INTERNAL_SERVER_ERROR)
                    .hasCauseExactlyInstanceOf(IOException.class);

            assertThatYouTubeRequest(fetchAction)
                    .hasFieldOrPropertyWithValue("requestType", YouTubeRequestEntity.RequestType.ACTIVITIES)
                    .hasFieldOrPropertyWithValue("objectId", fetchAction.getObjectId())
                    .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .hasFieldOrProperty("error");
        }
    }

    @Nested
    class Comments {

        @BeforeEach
        void beforeEach() {
            var fetchOperation = entityManager.persist(faker.database().fetchOperation().comments(fetchRequest));
            fetchAction = entityManager.persist(faker.database().fetchAction().comments(fetchOperation));
        }

        @ParameterizedTest
        @CsvSource({
                "200.json, 1",
                "200_empty.json, 0"
        })
        void getComments(String testResource, int expectedItemCount) {
            wireMock.stubForGetCommentThreads(fetchAction.getObjectId())
                    .willReturn(okJson(TEST_RESOURCES.load("comments", testResource)));

            var result = clientAdapter.getComments(fetchAction);

            assertThat(result).isNotNull();
            assertThat(result.getItems()).hasSize(expectedItemCount);
            assertThatYouTubeRequest(fetchAction)
                    .hasFieldOrPropertyWithValue("requestType", YouTubeRequestEntity.RequestType.COMMENTS)
                    .hasFieldOrPropertyWithValue("objectId", fetchAction.getObjectId())
                    .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.OK.value())
                    .hasFieldOrPropertyWithValue("itemCount", expectedItemCount);
        }

        @Test
        void getComments_commentsDisabled() {
            wireMock.stubForGetCommentThreads(fetchAction.getObjectId()).willReturn(forbidden()
                    .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .withBody(TEST_RESOURCES.load("comments", "403_comments_disabled.json")));

            assertThatThrownBy(() -> clientAdapter.getComments(fetchAction))
                    .isExactlyInstanceOf(CommentsDisabledException.class)
                    .hasFieldOrPropertyWithValue("status", HttpStatus.UNPROCESSABLE_ENTITY)
                    .hasCauseExactlyInstanceOf(GoogleJsonResponseException.class);

            assertThatYouTubeRequest(fetchAction)
                    .hasFieldOrPropertyWithValue("requestType", YouTubeRequestEntity.RequestType.COMMENTS)
                    .hasFieldOrPropertyWithValue("objectId", fetchAction.getObjectId())
                    .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.UNPROCESSABLE_ENTITY.value())
                    .hasFieldOrProperty("error");
        }

        @Test
        void getComments_fault() {
            wireMock.stubForGetCommentThreads(fetchAction.getObjectId())
                    .willReturn(aResponse().withFault(Fault.MALFORMED_RESPONSE_CHUNK));

            assertThatThrownBy(() -> clientAdapter.getComments(fetchAction))
                    .isInstanceOf(YouTubeException.class)
                    .hasFieldOrPropertyWithValue("status", HttpStatus.INTERNAL_SERVER_ERROR)
                    .hasCauseExactlyInstanceOf(IOException.class);

            assertThatYouTubeRequest(fetchAction)
                    .hasFieldOrPropertyWithValue("requestType", YouTubeRequestEntity.RequestType.COMMENTS)
                    .hasFieldOrPropertyWithValue("objectId", fetchAction.getObjectId())
                    .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .hasFieldOrProperty("error");
        }
    }

    @Nested
    class Replies {

        @BeforeEach
        void beforeEach() {
            var fetchOperation = entityManager.persist(faker.database().fetchOperation().replies(fetchRequest));
            fetchAction = entityManager.persist(faker.database().fetchAction().replies(fetchOperation));
        }

        @ParameterizedTest
        @CsvSource({
                "200.json, 1",
                "200_empty.json, 0"
        })
        void getReplies(String testResource, int expectedItemCount) {
            wireMock.stubForGetComments(fetchAction.getObjectId())
                    .willReturn(okJson(TEST_RESOURCES.load("replies", testResource)));

            var result = clientAdapter.getReplies(fetchAction);

            assertThat(result).isNotNull();
            assertThat(result.getItems()).hasSize(expectedItemCount);
            assertThatYouTubeRequest(fetchAction)
                    .hasFieldOrPropertyWithValue("requestType", YouTubeRequestEntity.RequestType.REPLIES)
                    .hasFieldOrPropertyWithValue("objectId", fetchAction.getObjectId())
                    .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.OK.value())
                    .hasFieldOrPropertyWithValue("itemCount", expectedItemCount);
        }

        @Test
        void getReplies_fault() {
            wireMock.stubForGetComments(fetchAction.getObjectId())
                    .willReturn(aResponse().withFault(Fault.MALFORMED_RESPONSE_CHUNK));

            assertThatThrownBy(() -> clientAdapter.getReplies(fetchAction))
                    .isInstanceOf(YouTubeException.class)
                    .hasFieldOrPropertyWithValue("status", HttpStatus.INTERNAL_SERVER_ERROR)
                    .hasCauseExactlyInstanceOf(IOException.class);

            assertThatYouTubeRequest(fetchAction)
                    .hasFieldOrPropertyWithValue("requestType", YouTubeRequestEntity.RequestType.REPLIES)
                    .hasFieldOrPropertyWithValue("objectId", fetchAction.getObjectId())
                    .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .hasFieldOrProperty("error");
        }
    }

    private ObjectAssert<YouTubeRequestEntity> assertThatYouTubeRequest(FetchActionEntity fetchAction) {
        var youTubeRequest = new YouTubeRequestEntity();
        youTubeRequest.setFetchActionId(fetchAction.getId());

        return assertThat(requestRepository.findOne(Example.of(youTubeRequest)))
                .isPresent()
                .get(new InstanceOfAssertFactory<>(YouTubeRequestEntity.class, Assertions::assertThat))
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("fetchActionId", fetchAction.getId())
                .hasFieldOrProperty("requestedAt")
                .hasFieldOrProperty("respondedAt");
    }
}
