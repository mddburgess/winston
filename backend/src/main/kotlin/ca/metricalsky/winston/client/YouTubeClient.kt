package ca.metricalsky.winston.client

import ca.metricalsky.winston.client.exception.wrap
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.ActivityListResponse
import com.google.api.services.youtube.model.ChannelListResponse
import com.google.api.services.youtube.model.CommentListResponse
import com.google.api.services.youtube.model.CommentThreadListResponse
import com.google.api.services.youtube.model.PlaylistItemListResponse
import com.google.api.services.youtube.model.VideoListResponse
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service

@Service
class YouTubeClient(
    private val youTube: YouTube,
) {
    private val log = KotlinLogging.logger {}

    fun getChannel(handle: String): ChannelListResponse {
        try {
            log.info { "Fetching channels for handle '${handle}'" }

            val response = youTube.channels()
                .list(channelParts)
                .setForHandle(handle)
                .setMaxResults(50L)
                .execute()

            val channelCount = response?.items?.size ?: 0
            log.info { "Fetched ${channelCount} channels for handle '${handle}'" }
            return response
        } catch (ex: Exception) {
            log.error(ex) { "Failed to fetch channels for handle '${handle}'" }
            throw wrap(ex)
        }
    }

    fun getActivities(
        channelId: String,
        publishedAfter: String? = null,
        publishedBefore: String? = null,
    ): ActivityListResponse {
        try {
            log.info {
                "Fetching activities for channelId '${channelId}'" +
                        " publishedAfter '${publishedAfter}' publishedBefore '${publishedBefore}'"
            }

            val response = youTube.activities()
                .list(activityParts)
                .setChannelId(channelId)
                .setPublishedAfter(publishedAfter)
                .setPublishedBefore(publishedBefore)
                .setMaxResults(50L)
                .execute()

            val count = response?.items?.size ?: 0
            log.info {
                "Fetched ${count} activities for channelId '${channelId}' " +
                        "publishedAfter '${publishedAfter}' publishedBefore '${publishedBefore}'"
            }
            return response
        } catch (ex: Exception) {
            log.error(ex) {
                "Failed to fetch activities for channelId '${channelId}'" +
                        " publishedAfter '${publishedAfter}' publishedBefore '${publishedBefore}'"
            }
            throw wrap(ex)
        }
    }

    fun getPlaylistItems(
        playlistId: String,
        pageToken: String? = null,
    ): PlaylistItemListResponse {
        try {
            log.info { "Fetching playlist items for playlistId '${playlistId}' pageToken '${pageToken}'" }

            val response = youTube.playlistItems()
                .list(playlistItemParts)
                .setPlaylistId(playlistId)
                .setPageToken(pageToken)
                .setMaxResults(50L)
                .execute()

            val count = response?.items?.size ?: 0
            log.info { "Fetched ${count} playlist items for playlistId '${playlistId}' pageToken '${pageToken}'" }
            return response
        } catch (ex: Exception) {
            log.error(ex) { "Failed to fetch playlist items for playlistId '${playlistId}' pageToken '${pageToken}'" }
            throw wrap(ex)
        }
    }

    fun getVideos(videoIds: List<String>): VideoListResponse {
        try {
            log.info { "Fetching videos for videoIds '${videoIds}'" }

            val response = youTube.videos()
                .list(videoParts)
                .setId(videoIds)
                .setMaxResults(50L)
                .execute()

            val count = response?.items?.size ?: 0
            log.info { "Fetched ${count} videos for videoIds ${videoIds}" }
            return response
        } catch (ex: Exception) {
            log.error(ex) { "Failed to fetch videos for videoIds '${videoIds}'" }
            throw wrap(ex)
        }
    }

    fun getComments(
        videoId: String,
        pageToken: String? = null,
    ): CommentThreadListResponse {
        try {
            log.info { "Fetching comments for videoIds '${videoId}' pageToken '${pageToken}'" }

            val response = youTube.commentThreads()
                .list(commentThreadParts)
                .setVideoId(videoId)
                .setPageToken(pageToken)
                .setMaxResults(100L)
                .execute()

            val count = response?.items?.size ?: 0
            log.info { "Fetched ${count} comments for videoId ${videoId}' pageToken '${pageToken}'" }
            return response
        } catch (ex: Exception) {
            log.error(ex) { "Failed to fetch comments for videoId '${videoId}' pageToken '${pageToken}'" }
            throw wrap(ex)
        }
    }

    fun getReplies(
        commentId: String,
        pageToken: String? = null,
    ): CommentListResponse {
        try {
            log.info { "Fetching replies for commentId '${commentId}' pageToken '${pageToken}'" }

            val response = youTube.comments()
                .list(commentParts)
                .setParentId(commentId)
                .setMaxResults(100L)
                .setPageToken(pageToken).execute()

            val count = response?.items?.size ?: 0
            log.info { "Fetched ${count} replies for commentId '${commentId}' pageToken '${pageToken}'" }
            return response
        } catch (ex: Exception) {
            log.error(ex) { "Failed to fetch replies for commentId '${commentId}' pageToken '${pageToken}'" }
            throw wrap(ex)
        }
    }
}

private val channelParts = listOf(
    "brandingSettings",
    "contentDetails",
    "contentOwnerDetails",
    "id",
    "localizations",
    "snippet",
    "statistics",
    "status",
    "topicDetails"
)

private val activityParts = listOf("contentDetails", "id", "snippet")

private val playlistItemParts = listOf("contentDetails", "id", "snippet", "status")

private val videoParts = listOf(
    "contentDetails",
    "id",
    "liveStreamingDetails",
    "localizations",
    "paidProductPlacementDetails",
    "recordingDetails",
    "snippet",
    "statistics",
    "status",
    "topicDetails"
)

private val commentThreadParts = listOf("id", "replies", "snippet")

private val commentParts = listOf("id", "snippet")
