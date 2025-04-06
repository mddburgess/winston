package ca.metricalsky.winston.client;

import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.including;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class YouTubeWireMock {

    private final String youtubeApiKey;

    public StubMappingBuilder stubForGetChannels(String forHandle) {
        return new StubMappingBuilder(get(urlPathEqualTo("/youtube/v3/channels"))
                .withQueryParam("part", including(YouTubeClient.CHANNEL_PARTS.toArray(new String[0])))
                .withQueryParam("forHandle", equalTo(forHandle))
                .withQueryParam("maxResults", equalTo("50"))
                .withQueryParam("key", equalTo(youtubeApiKey)));
    }

    public StubMappingBuilder stubForGetActivities(String channelId) {
        return new StubMappingBuilder(get(urlPathEqualTo("/youtube/v3/activities"))
                .withQueryParam("part", including(YouTubeClient.ACTIVITY_PARTS.toArray(new String[0])))
                .withQueryParam("channelId", equalTo(channelId))
                .withQueryParam("maxResults", equalTo("50"))
                .withQueryParam("key", equalTo(youtubeApiKey)));
    }

    public StubMappingBuilder stubForGetCommentThreads(String videoId) {
        return new StubMappingBuilder(get(urlPathEqualTo("/youtube/v3/commentThreads"))
                .withQueryParam("part", including(YouTubeClient.COMMENT_THREAD_PARTS.toArray(new String[0])))
                .withQueryParam("videoId", equalTo(videoId))
                .withQueryParam("maxResults", equalTo("100"))
                .withQueryParam("key", equalTo(youtubeApiKey)));
    }

    public StubMappingBuilder stubForGetComments(String parentId) {
        return new StubMappingBuilder(get(urlPathEqualTo("/youtube/v3/comments"))
                .withQueryParam("part", including(YouTubeClient.COMMENT_PARTS.toArray(new String[0])))
                .withQueryParam("parentId", equalTo(parentId))
                .withQueryParam("maxResults", equalTo("100"))
                .withQueryParam("key", equalTo(youtubeApiKey)));
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    static class StubMappingBuilder {

        private final MappingBuilder mappingBuilder;

        StubMapping willReturn(ResponseDefinitionBuilder responseDefBuilder) {
            return stubFor(mappingBuilder.willReturn(responseDefBuilder));
        }
    }
}
