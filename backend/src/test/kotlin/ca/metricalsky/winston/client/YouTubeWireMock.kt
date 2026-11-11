package ca.metricalsky.winston.client

import ca.metricalsky.winston.config.properties.youtube.YouTubeConfig
import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.including
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.github.tomakehurst.wiremock.stubbing.StubMapping

class YouTubeWireMock(
    private val youTubeConfig: YouTubeConfig,
) {
    val port = Regex("https?://.+:(\\d+)").find(youTubeConfig.mockRootUrl)?.groups[1]?.value?.toInt() ?: 0
    val wireMock: WireMock = WireMock.create().port(port).build()

    fun reset() = wireMock.resetMappings()

    fun stubForGetChannels(forHandle: String) = StubMappingBuilder(
        wireMock, get(urlPathEqualTo("/youtube/v3/channels"))
            .withQueryParam("part", including(*CHANNEL_PARTS.toTypedArray()))
            .withQueryParam("forHandle", equalTo(forHandle))
            .withQueryParam("maxResults", equalTo("50"))
            .withQueryParam("key", equalTo(youTubeConfig.apiKey))
    )

    fun stubForGetActivities(channelId: String) = StubMappingBuilder(
        wireMock, get(urlPathEqualTo("/youtube/v3/activities"))
            .withQueryParam("part", including(*ACTIVITY_PARTS.toTypedArray()))
            .withQueryParam("channelId", equalTo(channelId))
            .withQueryParam("maxResults", equalTo("50"))
            .withQueryParam("key", equalTo(youTubeConfig.apiKey))
    )

    fun stubForGetCommentThreads(videoId: String) = StubMappingBuilder(
        wireMock, get(urlPathEqualTo("/youtube/v3/commentThreads"))
            .withQueryParam("part", including(*COMMENT_THREAD_PARTS.toTypedArray()))
            .withQueryParam("videoId", equalTo(videoId))
            .withQueryParam("maxResults", equalTo("100"))
            .withQueryParam("key", equalTo(youTubeConfig.apiKey))
    )

    fun stubForGetComments(parentId: String) = StubMappingBuilder(
        wireMock, get(urlPathEqualTo("/youtube/v3/comments"))
            .withQueryParam("part", including(*COMMENT_PARTS.toTypedArray()))
            .withQueryParam("parentId", equalTo(parentId))
            .withQueryParam("maxResults", equalTo("100"))
            .withQueryParam("key", equalTo(youTubeConfig.apiKey))
    )

    class StubMappingBuilder(
        private val wireMock: WireMock,
        private val mappingBuilder: MappingBuilder,
    ) {
        fun willReturn(responseDefBuilder: ResponseDefinitionBuilder): StubMapping =
            wireMock.register(mappingBuilder.willReturn(responseDefBuilder))
    }
}
