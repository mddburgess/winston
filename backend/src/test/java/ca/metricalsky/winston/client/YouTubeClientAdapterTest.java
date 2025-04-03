package ca.metricalsky.winston.client;

import ca.metricalsky.winston.config.YouTubeConfig;
import ca.metricalsky.winston.entity.fetch.FetchAction;
import ca.metricalsky.winston.entity.fetch.FetchRequest;
import ca.metricalsky.winston.entity.fetch.YouTubeRequest;
import ca.metricalsky.winston.repository.fetch.YouTubeRequestRepository;
import ca.metricalsky.winston.test.TestResources;
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

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.including;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
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
    private static final String MAX_RESULTS = "50";
    private static final TestResources TEST_RESOURCES = TestResources.dir("client");

    @Value("${youtube.apiKey}")
    private String youtubeApiKey;

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private YouTubeClientAdapter clientAdapter;
    @Autowired
    private YouTubeRequestRepository requestRepository;

    @Test
    void getChannels() {
        stubFor(get(urlPathEqualTo("/youtube/v3/channels"))
                .withQueryParam("part", including(YouTubeClient.CHANNEL_PARTS.toArray(new String[0])))
                .withQueryParam("forHandle", equalTo(CHANNEL_HANDLE))
                .withQueryParam("maxResults", equalTo(MAX_RESULTS))
                .withQueryParam("key", equalTo(youtubeApiKey))
                .willReturn(okJson(TEST_RESOURCES.load("channels", "200.json"))));

        var fetchRequest = entityManager.persist(FetchRequest.builder()
                .fetchType(FetchRequest.FetchType.CHANNEL)
                .objectId("@handle")
                .status(FetchRequest.Status.FETCHING)
                .build());
        var fetchAction = entityManager.persist(FetchAction.builder()
                .fetchRequestId(fetchRequest.getId())
                .actionType(FetchAction.ActionType.CHANNELS)
                .objectId("@handle")
                .status(FetchAction.Status.FETCHING)
                .build());
        var youTubeRequest = YouTubeRequest.builder()
                .fetchActionId(fetchAction.getId())
                .requestType(YouTubeRequest.RequestType.CHANNELS)
                .objectId("@handle")
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
        stubFor(get(urlPathEqualTo("/youtube/v3/activities"))
                .withQueryParam("part", including(YouTubeClient.ACTIVITY_PARTS.toArray(new String[0])))
                .withQueryParam("channelId", equalTo("channelId"))
                .withQueryParam("maxResults", equalTo(MAX_RESULTS))
                .withQueryParam("key", equalTo(youtubeApiKey))
                .willReturn(okJson(TEST_RESOURCES.load("activities", "200.json"))));

        var fetchRequest = entityManager.persist(FetchRequest.builder()
                .fetchType(FetchRequest.FetchType.VIDEOS)
                .objectId("channelId")
                .status(FetchRequest.Status.FETCHING)
                .build());
        var fetchAction = entityManager.persist(FetchAction.builder()
                .fetchRequestId(fetchRequest.getId())
                .actionType(FetchAction.ActionType.VIDEOS)
                .objectId("channelId")
                .status(FetchAction.Status.FETCHING)
                .build());
        var youTubeRequest = YouTubeRequest.builder()
                .fetchActionId(fetchAction.getId())
                .requestType(YouTubeRequest.RequestType.ACTIVITIES)
                .objectId("channelId")
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
        stubFor(get(urlPathEqualTo("/youtube/v3/commentThreads"))
                .withQueryParam("part", including(YouTubeClient.COMMENT_THREAD_PARTS.toArray(new String[0])))
                .withQueryParam("videoId", equalTo("videoId"))
                .withQueryParam("maxResults", equalTo("100"))
                .withQueryParam("key", equalTo(youtubeApiKey))
                .willReturn(okJson(TEST_RESOURCES.load("comments", "200.json"))));

        var fetchRequest = entityManager.persist(FetchRequest.builder()
                .fetchType(FetchRequest.FetchType.COMMENTS)
                .objectId("videoId")
                .status(FetchRequest.Status.FETCHING)
                .build());
        var fetchAction = entityManager.persist(FetchAction.builder()
                .fetchRequestId(fetchRequest.getId())
                .actionType(FetchAction.ActionType.COMMENTS)
                .objectId("videoId")
                .status(FetchAction.Status.FETCHING)
                .build());
        var youTubeRequest = YouTubeRequest.builder()
                .fetchActionId(fetchAction.getId())
                .requestType(YouTubeRequest.RequestType.COMMENTS)
                .objectId("videoId")
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
        stubFor(get(urlPathEqualTo("/youtube/v3/comments"))
                .withQueryParam("part", including(YouTubeClient.COMMENT_PARTS.toArray(new String[0])))
                .withQueryParam("parentId", equalTo("commentId"))
                .withQueryParam("maxResults", equalTo("100"))
                .withQueryParam("key", equalTo(youtubeApiKey))
                .willReturn(okJson(TEST_RESOURCES.load("replies", "200.json"))));

        var fetchRequest = entityManager.persist(FetchRequest.builder()
                .fetchType(FetchRequest.FetchType.REPLIES)
                .objectId("commentId")
                .status(FetchRequest.Status.FETCHING)
                .build());
        var fetchAction = entityManager.persist(FetchAction.builder()
                .fetchRequestId(fetchRequest.getId())
                .actionType(FetchAction.ActionType.REPLIES)
                .objectId("commentId")
                .status(FetchAction.Status.FETCHING)
                .build());
        var youTubeRequest = YouTubeRequest.builder()
                .fetchActionId(fetchAction.getId())
                .requestType(YouTubeRequest.RequestType.REPLIES)
                .objectId("commentId")
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
}
