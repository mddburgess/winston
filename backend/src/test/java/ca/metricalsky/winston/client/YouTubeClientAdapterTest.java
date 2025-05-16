package ca.metricalsky.winston.client;

import ca.metricalsky.winston.config.YouTubeConfig;
import ca.metricalsky.winston.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.entity.fetch.FetchActionEntity.ActionType;
import ca.metricalsky.winston.entity.fetch.FetchRequestEntity;
import ca.metricalsky.winston.entity.fetch.FetchRequestEntity.FetchType;
import ca.metricalsky.winston.entity.fetch.YouTubeRequestEntity;
import ca.metricalsky.winston.repository.fetch.YouTubeRequestRepository;
import ca.metricalsky.winston.test.TestResources;
import com.github.tomakehurst.wiremock.http.Fault;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactory;
import org.assertj.core.api.ObjectAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
        YouTubeClientAdapter.class,
        YouTubeConfig.class,
        YouTubeRequestRepository.class,
}))
@EnableWireMock
class YouTubeClientAdapterTest {

    private static final String CHANNEL_HANDLE = "@handle";
    private static final String CHANNEL_ID = "channelId";
    private static final String VIDEO_ID = "videoId";
    private static final String COMMENT_ID = "commentId";

    private static final TestResources TEST_RESOURCES = TestResources.dir("client");

    @Value("${youtube.apiKey}")
    private String youtubeApiKey;

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private YouTubeClientAdapter clientAdapter;
    @Autowired
    private YouTubeRequestRepository requestRepository;

    private YouTubeWireMock wireMock;

    @BeforeEach
    void beforeEach() {
        wireMock = new YouTubeWireMock(youtubeApiKey);
    }

    @Test
    void getChannels() {
        wireMock.stubForGetChannels(CHANNEL_HANDLE)
                .willReturn(okJson(TEST_RESOURCES.load("channels", "200.json")));

        var fetchRequest = persistFetchRequest(FetchType.CHANNELS, CHANNEL_HANDLE);
        var fetchAction = persistFetchAction(fetchRequest, ActionType.CHANNELS, CHANNEL_HANDLE);

        var result = clientAdapter.getChannels(fetchAction);

        assertThat(result).isNotNull();
        assertThat(result.getItems()).hasSize(1);
        assertThatYouTubeRequest(fetchAction)
                .hasFieldOrPropertyWithValue("requestType", YouTubeRequestEntity.RequestType.CHANNELS)
                .hasFieldOrPropertyWithValue("objectId", CHANNEL_HANDLE)
                .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.OK.value())
                .hasFieldOrPropertyWithValue("itemCount", result.getItems().size());
    }

    @Test
    void getChannels_notFound() {
        wireMock.stubForGetChannels(CHANNEL_HANDLE)
                .willReturn(okJson(TEST_RESOURCES.load("channels", "200_not_found.json")));

        var fetchRequest = persistFetchRequest(FetchType.CHANNELS, CHANNEL_HANDLE);
        var fetchAction = persistFetchAction(fetchRequest, ActionType.CHANNELS, CHANNEL_HANDLE);

        var result = clientAdapter.getChannels(fetchAction);

        assertThat(result).isNotNull();
        assertThat(result.getItems()).isNull();
        assertThatYouTubeRequest(fetchAction)
                .hasFieldOrPropertyWithValue("requestType", YouTubeRequestEntity.RequestType.CHANNELS)
                .hasFieldOrPropertyWithValue("objectId", CHANNEL_HANDLE)
                .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.OK.value())
                .hasFieldOrPropertyWithValue("itemCount", 0);
    }

    @Test
    void getChannels_fault() {
        wireMock.stubForGetChannels(CHANNEL_HANDLE)
                .willReturn(aResponse().withFault(Fault.MALFORMED_RESPONSE_CHUNK));

        var fetchRequest = persistFetchRequest(FetchType.CHANNELS, CHANNEL_HANDLE);
        var fetchAction = persistFetchAction(fetchRequest, ActionType.CHANNELS, CHANNEL_HANDLE);

        assertThatThrownBy(() -> clientAdapter.getChannels(fetchAction))
                .isInstanceOf(YouTubeException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.INTERNAL_SERVER_ERROR)
                .hasCauseExactlyInstanceOf(IOException.class);

        assertThatYouTubeRequest(fetchAction)
                .hasFieldOrPropertyWithValue("requestType", YouTubeRequestEntity.RequestType.CHANNELS)
                .hasFieldOrPropertyWithValue("objectId", CHANNEL_HANDLE)
                .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.INTERNAL_SERVER_ERROR.value())
                .hasFieldOrProperty("error");
    }

    @ParameterizedTest
    @CsvSource({
            "200.json, 2",
            "200_empty.json, 0"
    })
    void getActivities(String testResource, int expectedItemCount) {
        wireMock.stubForGetActivities(CHANNEL_ID)
                .willReturn(okJson(TEST_RESOURCES.load("activities", testResource)));

        var fetchRequest = persistFetchRequest(FetchType.VIDEOS, CHANNEL_ID);
        var fetchAction = persistFetchAction(fetchRequest, ActionType.VIDEOS, CHANNEL_ID);

        var result = clientAdapter.getActivities(fetchAction);

        assertThat(result).isNotNull();
        assertThat(result.getItems()).hasSize(expectedItemCount);
        assertThatYouTubeRequest(fetchAction)
                .hasFieldOrPropertyWithValue("requestType", YouTubeRequestEntity.RequestType.ACTIVITIES)
                .hasFieldOrPropertyWithValue("objectId", CHANNEL_ID)
                .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.OK.value())
                .hasFieldOrPropertyWithValue("itemCount", expectedItemCount);
    }

    @Test
    void getActivities_fault() {
        wireMock.stubForGetActivities(CHANNEL_ID)
                .willReturn(aResponse().withFault(Fault.MALFORMED_RESPONSE_CHUNK));

        var fetchRequest = persistFetchRequest(FetchType.VIDEOS, CHANNEL_ID);
        var fetchAction = persistFetchAction(fetchRequest, ActionType.VIDEOS, CHANNEL_ID);

        assertThatThrownBy(() -> clientAdapter.getActivities(fetchAction))
                .isInstanceOf(YouTubeException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.INTERNAL_SERVER_ERROR)
                .hasCauseExactlyInstanceOf(IOException.class);

        assertThatYouTubeRequest(fetchAction)
                .hasFieldOrPropertyWithValue("requestType", YouTubeRequestEntity.RequestType.ACTIVITIES)
                .hasFieldOrPropertyWithValue("objectId", CHANNEL_ID)
                .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.INTERNAL_SERVER_ERROR.value())
                .hasFieldOrProperty("error");
    }

    @ParameterizedTest
    @CsvSource({
            "200.json, 1",
            "200_empty.json, 0"
    })
    void getComments(String testResource, int expectedItemCount) {
        wireMock.stubForGetCommentThreads(VIDEO_ID)
                .willReturn(okJson(TEST_RESOURCES.load("comments", testResource)));

        var fetchRequest = persistFetchRequest(FetchType.COMMENTS, VIDEO_ID);
        var fetchAction = persistFetchAction(fetchRequest, ActionType.COMMENTS, VIDEO_ID);

        var result = clientAdapter.getComments(fetchAction);

        assertThat(result).isNotNull();
        assertThat(result.getItems()).hasSize(expectedItemCount);
        assertThatYouTubeRequest(fetchAction)
                .hasFieldOrPropertyWithValue("requestType", YouTubeRequestEntity.RequestType.COMMENTS)
                .hasFieldOrPropertyWithValue("objectId", VIDEO_ID)
                .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.OK.value())
                .hasFieldOrPropertyWithValue("itemCount", expectedItemCount);
    }

    @Test
    void getComments_commentsDisabled() {
        wireMock.stubForGetCommentThreads(VIDEO_ID).willReturn(forbidden()
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(TEST_RESOURCES.load("comments", "403_comments_disabled.json")));

        var fetchRequest = persistFetchRequest(FetchType.COMMENTS, VIDEO_ID);
        var fetchAction = persistFetchAction(fetchRequest, ActionType.COMMENTS, VIDEO_ID);

        assertThatThrownBy(() -> clientAdapter.getComments(fetchAction))
                .isExactlyInstanceOf(CommentsDisabledException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.UNPROCESSABLE_ENTITY)
                .hasCauseExactlyInstanceOf(GoogleJsonResponseException.class);

        assertThatYouTubeRequest(fetchAction)
                .hasFieldOrPropertyWithValue("requestType", YouTubeRequestEntity.RequestType.COMMENTS)
                .hasFieldOrPropertyWithValue("objectId", VIDEO_ID)
                .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.UNPROCESSABLE_ENTITY.value())
                .hasFieldOrProperty("error");
    }

    @Test
    void getComments_fault() {
        wireMock.stubForGetCommentThreads(VIDEO_ID)
                .willReturn(aResponse().withFault(Fault.MALFORMED_RESPONSE_CHUNK));

        var fetchRequest = persistFetchRequest(FetchType.COMMENTS, VIDEO_ID);
        var fetchAction = persistFetchAction(fetchRequest, ActionType.COMMENTS, VIDEO_ID);

        assertThatThrownBy(() -> clientAdapter.getComments(fetchAction))
                .isInstanceOf(YouTubeException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.INTERNAL_SERVER_ERROR)
                .hasCauseExactlyInstanceOf(IOException.class);

        assertThatYouTubeRequest(fetchAction)
                .hasFieldOrPropertyWithValue("requestType", YouTubeRequestEntity.RequestType.COMMENTS)
                .hasFieldOrPropertyWithValue("objectId", VIDEO_ID)
                .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.INTERNAL_SERVER_ERROR.value())
                .hasFieldOrProperty("error");
    }

    @ParameterizedTest
    @CsvSource({
            "200.json, 1",
            "200_empty.json, 0"
    })
    void getReplies(String testResource, int expectedItemCount) {
        wireMock.stubForGetComments(COMMENT_ID)
                .willReturn(okJson(TEST_RESOURCES.load("replies", testResource)));

        var fetchRequest = persistFetchRequest(FetchType.REPLIES, COMMENT_ID);
        var fetchAction = persistFetchAction(fetchRequest, ActionType.COMMENTS, COMMENT_ID);

        var result = clientAdapter.getReplies(fetchAction);

        assertThat(result).isNotNull();
        assertThat(result.getItems()).hasSize(expectedItemCount);
        assertThatYouTubeRequest(fetchAction)
                .hasFieldOrPropertyWithValue("requestType", YouTubeRequestEntity.RequestType.REPLIES)
                .hasFieldOrPropertyWithValue("objectId", COMMENT_ID)
                .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.OK.value())
                .hasFieldOrPropertyWithValue("itemCount", expectedItemCount);
    }

    @Test
    void getReplies_fault() {
        wireMock.stubForGetComments(COMMENT_ID)
                .willReturn(aResponse().withFault(Fault.MALFORMED_RESPONSE_CHUNK));

        var fetchRequest = persistFetchRequest(FetchType.REPLIES, COMMENT_ID);
        var fetchAction = persistFetchAction(fetchRequest, ActionType.COMMENTS, COMMENT_ID);

        assertThatThrownBy(() -> clientAdapter.getReplies(fetchAction))
                .isInstanceOf(YouTubeException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.INTERNAL_SERVER_ERROR)
                .hasCauseExactlyInstanceOf(IOException.class);

        assertThatYouTubeRequest(fetchAction)
                .hasFieldOrPropertyWithValue("requestType", YouTubeRequestEntity.RequestType.REPLIES)
                .hasFieldOrPropertyWithValue("objectId", COMMENT_ID)
                .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.INTERNAL_SERVER_ERROR.value())
                .hasFieldOrProperty("error");
    }

    private FetchRequestEntity persistFetchRequest(FetchType fetchType, String objectId) {
        return entityManager.persist(FetchRequestEntity.builder()
                .fetchType(fetchType)
                .objectId(objectId)
                .status(FetchRequestEntity.Status.FETCHING)
                .build());
    }

    private FetchActionEntity persistFetchAction(FetchRequestEntity fetchRequest, ActionType actionType, String objectId) {
        return entityManager.persist(FetchActionEntity.builder()
                .fetchRequestId(fetchRequest.getId())
                .actionType(actionType)
                .objectId(objectId)
                .status(FetchActionEntity.Status.FETCHING)
                .build());
    }

    private ObjectAssert<YouTubeRequestEntity> assertThatYouTubeRequest(FetchActionEntity fetchAction) {
        var youTubeRequest = YouTubeRequestEntity.builder()
                .fetchActionId(fetchAction.getId())
                .build();
        return assertThat(requestRepository.findOne(Example.of(youTubeRequest)))
                .isPresent()
                .get(new InstanceOfAssertFactory<>(YouTubeRequestEntity.class, Assertions::assertThat))
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("fetchActionId", fetchAction.getId())
                .hasFieldOrProperty("requestedAt")
                .hasFieldOrProperty("respondedAt");
    }
}
