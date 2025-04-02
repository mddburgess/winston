package ca.metricalsky.winston.client;

import ca.metricalsky.winston.config.YouTubeConfig;
import ca.metricalsky.winston.test.TestResources;
import com.google.api.client.util.DateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.wiremock.spring.EnableWireMock;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.including;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {
        YouTubeClient.class,
        YouTubeConfig.class,
})
@EnableWireMock
class YouTubeClientTest {

    private static final String CHANNEL_HANDLE = "@handle";
    private static final String MAX_RESULTS = "50";
    private static final TestResources TEST_RESOURCES = TestResources.dir("client", "channels");

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
                .willReturn(okJson(TEST_RESOURCES.load("200.json"))));

        var result = youTubeClient.getChannel(CHANNEL_HANDLE);

        assertThat(result).isNotNull();
        assertThat(result.getItems())
                .hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue("snippet.title", "channel.snippet.title")
                .hasFieldOrPropertyWithValue("snippet.description", "channel.snippet.description")
                .hasFieldOrPropertyWithValue("snippet.customUrl", "@channelCustomUrl")
                .hasFieldOrPropertyWithValue("snippet.publishedAt", new DateTime("2025-01-01T00:00:00Z"))
                .hasFieldOrPropertyWithValue("snippet.thumbnails.high.url",
                        "https://www.example.com/channel/snippet/thumbnails/high")
                .hasFieldOrPropertyWithValue("topicDetails.topicCategories",
                        List.of("https://en.wikipedia.org/wiki/Topic", "https://en.wikipedia.org/wiki/Category"))
                .hasFieldOrPropertyWithValue("brandingSettings.channel.keywords",
                        "channel \"branding settings\" keywords");
    }

    @Test
    void getChannel_notFound() throws Exception {
        stubFor(get(urlPathEqualTo("/youtube/v3/channels"))
                .withQueryParam("part", including(YouTubeClient.CHANNEL_PARTS.toArray(new String[0])))
                .withQueryParam("forHandle", equalTo(CHANNEL_HANDLE))
                .withQueryParam("maxResults", equalTo(MAX_RESULTS))
                .withQueryParam("key", equalTo(youtubeApiKey))
                .willReturn(okJson(TEST_RESOURCES.load("200_not_found.json"))));

        var result = youTubeClient.getChannel(CHANNEL_HANDLE);

        assertThat(result).isNotNull();
        assertThat(result.getItems()).isNull();
    }
}
