package ca.metricalsky.winston.client

import ca.metricalsky.winston.config.properties.youtube.YouTubeConfig
import com.github.tomakehurst.wiremock.client.WireMock
import com.marcinziolo.kotlin.wiremock.contains
import com.marcinziolo.kotlin.wiremock.equalTo
import com.marcinziolo.kotlin.wiremock.get

class YouTubeWireMock(
    youTubeConfig: YouTubeConfig,
) {
    val wireMock: WireMock = youTubeWireMock(youTubeConfig)

    fun reset() = wireMock.resetMappings()

    fun getActivitiesFor(channelId: String) = wireMock.get {
        urlPath equalTo "/youtube/v3/activities"
        queryParams contains "channelId" equalTo channelId
    }

    fun getChannelsFor(channelHandle: String) = wireMock.get {
        urlPath equalTo "/youtube/v3/channels"
        queryParams contains "forHandle" equalTo channelHandle
    }

    fun getCommentsFor(parentId: String) = wireMock.get {
        urlPath equalTo "/youtube/v3/comments"
        queryParams contains "parentId" equalTo parentId
    }

    fun getCommentThreadsFor(videoId: String) = wireMock.get {
        urlPath equalTo "/youtube/v3/commentThreads"
        queryParams contains "videoId" equalTo videoId
    }
}

private fun youTubeWireMock(youTubeConfig: YouTubeConfig): WireMock {
    val port = Regex("https?://.+:(\\d+)").find(youTubeConfig.mockRootUrl)?.groups[1]?.value?.toInt() ?: 0
    return WireMock.create().port(port).build()
}
