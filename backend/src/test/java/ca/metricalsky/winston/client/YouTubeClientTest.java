package ca.metricalsky.winston.client;

import ca.metricalsky.winston.config.YouTubeConfig;
import ca.metricalsky.winston.test.TestResources;
import com.github.tomakehurst.wiremock.http.Fault;
import com.google.api.client.util.DateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.wiremock.spring.EnableWireMock;

import java.io.IOException;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.including;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = {
        YouTubeClient.class,
        YouTubeConfig.class,
})
@EnableWireMock
class YouTubeClientTest {

    private static final String CHANNEL_HANDLE = "@handle";
    private static final String MAX_RESULTS = "50";
    private static final TestResources TEST_RESOURCES = TestResources.dir("client");

    @Value("${youtube.apiKey}")
    private String youtubeApiKey;

    @Autowired
    private YouTubeClient youTubeClient;

    @Test
    void getChannel() throws Exception {
        stubFor(get(urlPathEqualTo("/youtube/v3/channels"))
                .withQueryParam("part", including(YouTubeClient.CHANNEL_PARTS.toArray(new String[0])))
                .withQueryParam("forHandle", equalTo(CHANNEL_HANDLE))
                .withQueryParam("maxResults", equalTo(MAX_RESULTS))
                .withQueryParam("key", equalTo(youtubeApiKey))
                .willReturn(okJson(TEST_RESOURCES.load("channels", "200.json"))));

        var result = youTubeClient.getChannel(CHANNEL_HANDLE);

        assertThat(result).isNotNull();
        assertThat(result.getItems())
                .hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue("brandingSettings.channel.keywords",
                        "channel \"branding settings\" keywords")
                .hasFieldOrPropertyWithValue("snippet.customUrl", "@channelCustomUrl")
                .hasFieldOrPropertyWithValue("snippet.description", "channel.snippet.description")
                .hasFieldOrPropertyWithValue("snippet.publishedAt", new DateTime("2025-01-01T00:00:00Z"))
                .hasFieldOrPropertyWithValue("snippet.thumbnails.high.url",
                        "https://www.example.com/channel/snippet/thumbnails/high")
                .hasFieldOrPropertyWithValue("snippet.title", "channel.snippet.title")
                .hasFieldOrPropertyWithValue("topicDetails.topicCategories",
                        List.of("https://en.wikipedia.org/wiki/Topic", "https://en.wikipedia.org/wiki/Category"));
    }

    @Test
    void getChannel_notFound() throws Exception {
        stubFor(get(urlPathEqualTo("/youtube/v3/channels"))
                .withQueryParam("part", including(YouTubeClient.CHANNEL_PARTS.toArray(new String[0])))
                .withQueryParam("forHandle", equalTo(CHANNEL_HANDLE))
                .withQueryParam("maxResults", equalTo(MAX_RESULTS))
                .withQueryParam("key", equalTo(youtubeApiKey))
                .willReturn(okJson(TEST_RESOURCES.load("channels", "200_not_found.json"))));

        var result = youTubeClient.getChannel(CHANNEL_HANDLE);

        assertThat(result).isNotNull();
        assertThat(result.getItems()).isNull();
    }

    @Test
    void getChannel_fault() {
        stubFor(get(urlPathEqualTo("/youtube/v3/channels"))
                .withQueryParam("part", including(YouTubeClient.CHANNEL_PARTS.toArray(new String[0])))
                .withQueryParam("forHandle", equalTo(CHANNEL_HANDLE))
                .withQueryParam("maxResults", equalTo(MAX_RESULTS))
                .withQueryParam("key", equalTo(youtubeApiKey))
                .willReturn(aResponse().withFault(Fault.MALFORMED_RESPONSE_CHUNK)));

        assertThatThrownBy(() -> youTubeClient.getChannel(CHANNEL_HANDLE))
                .isInstanceOf(YouTubeException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.INTERNAL_SERVER_ERROR)
                .hasCauseExactlyInstanceOf(IOException.class);
    }

    @Test
    void getActivities() throws Exception {
        stubFor(get(urlPathEqualTo("/youtube/v3/activities"))
                .withQueryParam("part", including(YouTubeClient.ACTIVITY_PARTS.toArray(new String[0])))
                .withQueryParam("channelId", equalTo("channelId"))
                .withQueryParam("maxResults", equalTo(MAX_RESULTS))
                .withQueryParam("key", equalTo(youtubeApiKey))
                .willReturn(okJson(TEST_RESOURCES.load("activities", "200.json"))));

        var result = youTubeClient.getActivities("channelId", null, null);

        assertThat(result).isNotNull();
        assertThat(result.getItems()).hasSize(2);

        var videoActivity = result.getItems().getFirst();
        assertThat(videoActivity)
                .hasFieldOrPropertyWithValue("contentDetails.upload.videoId",
                        "videoActivity.contentDetails.upload.videoId")
                .hasFieldOrPropertyWithValue("snippet.channelId", "videoActivity.snippet.channelId")
                .hasFieldOrPropertyWithValue("snippet.description", "videoActivity.snippet.description")
                .hasFieldOrPropertyWithValue("snippet.publishedAt", new DateTime("2025-01-01T00:00:00Z"))
                .hasFieldOrPropertyWithValue("snippet.thumbnails.high.url",
                        "https://www.example.com/videoActivity/snippet/thumbnails/high")
                .hasFieldOrPropertyWithValue("snippet.title", "videoActivity.snippet.title");

        var playlistActivity = result.getItems().getLast();
        assertThat(playlistActivity)
                .hasFieldOrPropertyWithValue("contentDetails.playlistItem.playlistId",
                        "playlistActivity.contentDetails.playlistItem.playlistId")
                .hasFieldOrPropertyWithValue("snippet.channelId", "playlistActivity.snippet.channelId")
                .hasFieldOrPropertyWithValue("snippet.description", "playlistActivity.snippet.description")
                .hasFieldOrPropertyWithValue("snippet.publishedAt", new DateTime("2025-01-01T00:00:00Z"))
                .hasFieldOrPropertyWithValue("snippet.thumbnails.high.url",
                        "https://www.example.com/playlistActivity/snippet/thumbnails/high")
                .hasFieldOrPropertyWithValue("snippet.title", "playlistActivity.snippet.title");
    }

    @Test
    void getActivities_empty() throws Exception {
        stubFor(get(urlPathEqualTo("/youtube/v3/activities"))
                .withQueryParam("part", including(YouTubeClient.ACTIVITY_PARTS.toArray(new String[0])))
                .withQueryParam("channelId", equalTo("channelId"))
                .withQueryParam("maxResults", equalTo(MAX_RESULTS))
                .withQueryParam("key", equalTo(youtubeApiKey))
                .willReturn(okJson(TEST_RESOURCES.load("activities", "200_empty.json"))));

        var result = youTubeClient.getActivities("channelId", null, null);

        assertThat(result).isNotNull();
        assertThat(result.getItems()).isEmpty();
    }

    @Test
    void getActivities_fault() {
        stubFor(get(urlPathEqualTo("/youtube/v3/activities"))
                .withQueryParam("part", including(YouTubeClient.ACTIVITY_PARTS.toArray(new String[0])))
                .withQueryParam("channelId", equalTo("channelId"))
                .withQueryParam("maxResults", equalTo(MAX_RESULTS))
                .withQueryParam("key", equalTo(youtubeApiKey))
                .willReturn(aResponse().withFault(Fault.MALFORMED_RESPONSE_CHUNK)));

        assertThatThrownBy(() -> youTubeClient.getActivities("channelId", null, null))
                .isInstanceOf(YouTubeException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.INTERNAL_SERVER_ERROR)
                .hasCauseExactlyInstanceOf(IOException.class);
    }

    @Test
    void getComments() throws Exception {
        stubFor(get(urlPathEqualTo("/youtube/v3/commentThreads"))
                .withQueryParam("part", including(YouTubeClient.COMMENT_THREAD_PARTS.toArray(new String[0])))
                .withQueryParam("videoId", equalTo("videoId"))
                .withQueryParam("maxResults", equalTo("100"))
                .withQueryParam("key", equalTo(youtubeApiKey))
                .willReturn(okJson(TEST_RESOURCES.load("comments", "200.json"))));

        var result = youTubeClient.getComments("videoId", null);

        assertThat(result).isNotNull();
        assertThat(result.getItems())
                .hasSize(1)
                .first()
                .hasFieldOrProperty("snippet.topLevelComment");

        var topLevelComment = result.getItems().getFirst().getSnippet().getTopLevelComment();
        assertThat(topLevelComment)
                .hasFieldOrPropertyWithValue("snippet.authorChannelId.value",
                        "topLevelComment.snippet.authorChannelId.value")
                .hasFieldOrPropertyWithValue("snippet.authorChannelUrl",
                        "https://www.example.com/topLevelComment/snippet/authorChannelUrl")
                .hasFieldOrPropertyWithValue("snippet.authorProfileImageUrl",
                        "https://www.example.com/topLevelComment/snippet/authorProfileImageUrl")
                .hasFieldOrPropertyWithValue("snippet.authorDisplayName", "@topLevelCommentAuthorDisplayName")
                .hasFieldOrPropertyWithValue("snippet.publishedAt", new DateTime("2025-01-01T00:00:00Z"))
                .hasFieldOrPropertyWithValue("snippet.textDisplay", "topLevelComment.snippet.textDisplay")
                .hasFieldOrPropertyWithValue("snippet.textOriginal", "topLevelComment.snippet.textOriginal")
                .hasFieldOrPropertyWithValue("snippet.updatedAt", new DateTime("2025-01-02T00:00:00Z"))
                .hasFieldOrPropertyWithValue("snippet.videoId", "topLevelComment.snippet.videoId");
    }

    @Test
    void getComments_empty() throws Exception {
        stubFor(get(urlPathEqualTo("/youtube/v3/commentThreads"))
                .withQueryParam("part", including(YouTubeClient.COMMENT_THREAD_PARTS.toArray(new String[0])))
                .withQueryParam("videoId", equalTo("videoId"))
                .withQueryParam("maxResults", equalTo("100"))
                .withQueryParam("key", equalTo(youtubeApiKey))
                .willReturn(okJson(TEST_RESOURCES.load("comments", "200_empty.json"))));

        var result = youTubeClient.getComments("videoId", null);

        assertThat(result).isNotNull();
        assertThat(result.getItems()).isEmpty();
    }

    @Test
    void getComments_fault() {
        stubFor(get(urlPathEqualTo("/youtube/v3/commentThreads"))
                .withQueryParam("part", including(YouTubeClient.COMMENT_THREAD_PARTS.toArray(new String[0])))
                .withQueryParam("videoId", equalTo("videoId"))
                .withQueryParam("maxResults", equalTo("100"))
                .withQueryParam("key", equalTo(youtubeApiKey))
                .willReturn(aResponse().withFault(Fault.MALFORMED_RESPONSE_CHUNK)));

        assertThatThrownBy(() -> youTubeClient.getComments("videoId", null))
                .isInstanceOf(YouTubeException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.INTERNAL_SERVER_ERROR)
                .hasCauseExactlyInstanceOf(IOException.class);
    }

    @Test
    void getReplies() throws Exception {
        stubFor(get(urlPathEqualTo("/youtube/v3/comments"))
                .withQueryParam("part", including(YouTubeClient.COMMENT_PARTS.toArray(new String[0])))
                .withQueryParam("parentId", equalTo("commentId"))
                .withQueryParam("maxResults", equalTo("100"))
                .withQueryParam("key", equalTo(youtubeApiKey))
                .willReturn(okJson(TEST_RESOURCES.load("replies", "200.json"))));

        var result = youTubeClient.getReplies("commentId", null);

        assertThat(result).isNotNull();
        assertThat(result.getItems())
                .hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue("snippet.authorChannelId.value",
                        "comment.snippet.authorChannelId.value")
                .hasFieldOrPropertyWithValue("snippet.authorChannelUrl",
                        "https://www.example.com/comment/snippet/authorChannelUrl")
                .hasFieldOrPropertyWithValue("snippet.authorProfileImageUrl",
                        "https://www.example.com/comment/snippet/authorProfileImageUrl")
                .hasFieldOrPropertyWithValue("snippet.authorDisplayName", "@commentAuthorDisplayName")
                .hasFieldOrPropertyWithValue("snippet.parentId", "comment.snippet.parentId")
                .hasFieldOrPropertyWithValue("snippet.publishedAt", new DateTime("2025-01-01T00:00:00Z"))
                .hasFieldOrPropertyWithValue("snippet.textDisplay", "comment.snippet.textDisplay")
                .hasFieldOrPropertyWithValue("snippet.textOriginal", "comment.snippet.textOriginal")
                .hasFieldOrPropertyWithValue("snippet.updatedAt", new DateTime("2025-01-02T00:00:00Z"));
    }

    @Test
    void getReplies_empty() throws Exception {
        stubFor(get(urlPathEqualTo("/youtube/v3/comments"))
                .withQueryParam("part", including(YouTubeClient.COMMENT_PARTS.toArray(new String[0])))
                .withQueryParam("parentId", equalTo("commentId"))
                .withQueryParam("maxResults", equalTo("100"))
                .withQueryParam("key", equalTo(youtubeApiKey))
                .willReturn(okJson(TEST_RESOURCES.load("replies", "200_empty.json"))));

        var result = youTubeClient.getReplies("commentId", null);

        assertThat(result).isNotNull();
        assertThat(result.getItems()).isEmpty();
    }

    @Test
    void getReplies_fault() {
        stubFor(get(urlPathEqualTo("/youtube/v3/comments"))
                .withQueryParam("part", including(YouTubeClient.COMMENT_PARTS.toArray(new String[0])))
                .withQueryParam("parentId", equalTo("commentId"))
                .withQueryParam("maxResults", equalTo("100"))
                .withQueryParam("key", equalTo(youtubeApiKey))
                .willReturn(aResponse().withFault(Fault.MALFORMED_RESPONSE_CHUNK)));

        assertThatThrownBy(() -> youTubeClient.getReplies("commentId", null))
                .isInstanceOf(YouTubeException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.INTERNAL_SERVER_ERROR)
                .hasCauseExactlyInstanceOf(IOException.class);
    }
}
