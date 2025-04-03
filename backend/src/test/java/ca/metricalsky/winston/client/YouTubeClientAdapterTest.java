package ca.metricalsky.winston.client;

import ca.metricalsky.winston.config.YouTubeConfig;
import ca.metricalsky.winston.entity.fetch.FetchAction;
import ca.metricalsky.winston.entity.fetch.FetchAction.ActionType;
import ca.metricalsky.winston.entity.fetch.FetchRequest;
import ca.metricalsky.winston.entity.fetch.FetchRequest.FetchType;
import ca.metricalsky.winston.entity.fetch.YouTubeRequest;
import ca.metricalsky.winston.repository.fetch.YouTubeRequestRepository;
import ca.metricalsky.winston.test.TestResources;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.wiremock.spring.EnableWireMock;

import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static org.assertj.core.api.Assertions.assertThat;

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

        var fetchRequest = persistFetchRequest(FetchType.CHANNEL, CHANNEL_HANDLE);
        var fetchAction = persistFetchAction(fetchRequest, ActionType.CHANNELS, CHANNEL_HANDLE);
        var youTubeRequest = YouTubeRequest.builder()
                .fetchActionId(fetchAction.getId())
                .requestType(YouTubeRequest.RequestType.CHANNELS)
                .objectId(CHANNEL_HANDLE)
                .build();

        var result = clientAdapter.getChannels(youTubeRequest);

        assertThat(result).isNotNull();
        assertThat(result.getItems()).hasSize(1);
        assertThat(requestRepository.findOne(Example.of(youTubeRequest)))
                .isPresent()
                .get()
                .hasFieldOrProperty("id")
                .hasFieldOrProperty("requestedAt")
                .hasFieldOrProperty("respondedAt")
                .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.OK.value())
                .hasFieldOrPropertyWithValue("itemCount", result.getItems().size());
    }

    @Test
    void getActivities() {
        wireMock.stubForGetActivities(CHANNEL_ID)
                .willReturn(okJson(TEST_RESOURCES.load("activities", "200.json")));

        var fetchRequest = persistFetchRequest(FetchType.VIDEOS, CHANNEL_ID);
        var fetchAction = persistFetchAction(fetchRequest, ActionType.VIDEOS, CHANNEL_ID);
        var youTubeRequest = YouTubeRequest.builder()
                .fetchActionId(fetchAction.getId())
                .requestType(YouTubeRequest.RequestType.ACTIVITIES)
                .objectId(CHANNEL_ID)
                .build();

        var result = clientAdapter.getActivities(youTubeRequest);

        assertThat(result).isNotNull();
        assertThat(result.getItems()).hasSize(2);
        assertThat(requestRepository.findOne(Example.of(youTubeRequest)))
                .isPresent()
                .get()
                .hasFieldOrProperty("id")
                .hasFieldOrProperty("requestedAt")
                .hasFieldOrProperty("respondedAt")
                .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.OK.value())
                .hasFieldOrPropertyWithValue("itemCount", result.getItems().size());
    }

    @Test
    void getComments() {
        wireMock.stubForGetCommentThreads(VIDEO_ID)
                .willReturn(okJson(TEST_RESOURCES.load("comments", "200.json")));

        var fetchRequest = persistFetchRequest(FetchType.COMMENTS, VIDEO_ID);
        var fetchAction = persistFetchAction(fetchRequest, ActionType.COMMENTS, VIDEO_ID);
        var youTubeRequest = YouTubeRequest.builder()
                .fetchActionId(fetchAction.getId())
                .requestType(YouTubeRequest.RequestType.COMMENTS)
                .objectId(VIDEO_ID)
                .build();

        var result = clientAdapter.getComments(youTubeRequest);

        assertThat(result).isNotNull();
        assertThat(result.getItems()).hasSize(1);
        assertThat(requestRepository.findOne(Example.of(youTubeRequest)))
                .isPresent()
                .get()
                .hasFieldOrProperty("id")
                .hasFieldOrProperty("requestedAt")
                .hasFieldOrProperty("respondedAt")
                .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.OK.value())
                .hasFieldOrPropertyWithValue("itemCount", result.getItems().size());
    }

    @Test
    void getReplies() {
        wireMock.stubForGetComments(COMMENT_ID)
                .willReturn(okJson(TEST_RESOURCES.load("replies", "200.json")));

        var fetchRequest = persistFetchRequest(FetchType.REPLIES, COMMENT_ID);
        var fetchAction = persistFetchAction(fetchRequest, ActionType.COMMENTS, COMMENT_ID);
        var youTubeRequest = YouTubeRequest.builder()
                .fetchActionId(fetchAction.getId())
                .requestType(YouTubeRequest.RequestType.REPLIES)
                .objectId(COMMENT_ID)
                .build();

        var result = clientAdapter.getReplies(youTubeRequest);

        assertThat(result).isNotNull();
        assertThat(result.getItems()).hasSize(1);
        assertThat(requestRepository.findOne(Example.of(youTubeRequest)))
                .isPresent()
                .get()
                .hasFieldOrProperty("id")
                .hasFieldOrProperty("requestedAt")
                .hasFieldOrProperty("respondedAt")
                .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.OK.value())
                .hasFieldOrPropertyWithValue("itemCount", result.getItems().size());
    }

    private FetchRequest persistFetchRequest(FetchType fetchType, String objectId) {
        return entityManager.persist(FetchRequest.builder()
                .fetchType(fetchType)
                .objectId(objectId)
                .status(FetchRequest.Status.FETCHING)
                .build());
    }

    private FetchAction persistFetchAction(FetchRequest fetchRequest, ActionType actionType, String objectId) {
        return entityManager.persist(FetchAction.builder()
                .fetchRequestId(fetchRequest.getId())
                .actionType(actionType)
                .objectId(objectId)
                .status(FetchAction.Status.FETCHING)
                .build());
    }
}
