package ca.metricalsky.winston.service

import ca.metricalsky.winston.client.YouTubeClient
import ca.metricalsky.winston.client.exception.ProcessingFailureException
import ca.metricalsky.winston.client.exception.YouTubeException
import ca.metricalsky.winston.database.entity.fetch.FetchActionEntity
import ca.metricalsky.winston.database.entity.fetch.YouTubeRequestEntity
import ca.metricalsky.winston.database.entity.fetch.YouTubeRequestEntity.RequestType
import ca.metricalsky.winston.database.repository.fetch.YouTubeRequestRepository
import org.springframework.http.HttpStatus
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@Service
@Retryable(retryFor = [ProcessingFailureException::class])
class YouTubeService(
    private val youTubeClient: YouTubeClient,
    private val youTubeRequestRepository: YouTubeRequestRepository,
) {

    fun getChannels(fetchAction: FetchActionEntity) = callYouTube(
        YouTubeRequestEntity(
            fetchActionId = fetchAction.id,
            requestType = RequestType.CHANNELS,
            objectId = fetchAction.objectId,
        )
    ) {
        getChannel(
            handle = it.objectId,
        ).apply {
            it.itemCount = items.orEmpty().size
        }
    }

    fun getActivities(fetchAction: FetchActionEntity) = callYouTube(
        YouTubeRequestEntity(
            fetchActionId = fetchAction.id,
            requestType = RequestType.ACTIVITIES,
            objectId = fetchAction.objectId,
            publishedAfter = fetchAction.publishedAfter.toIsoInstantString(),
            publishedBefore = fetchAction.publishedBefore.toIsoInstantString(),
        )
    ) {
        getActivities(
            channelId = it.objectId,
            publishedAfter = it.publishedAfter,
            publishedBefore = it.publishedBefore,
        ).apply {
            it.itemCount = items.orEmpty().size
        }
    }

    fun getPlaylistItems(fetchAction: FetchActionEntity) = callYouTube(
        YouTubeRequestEntity(
            fetchActionId = fetchAction.id,
            requestType = RequestType.PLAYLIST_ITEMS,
            objectId = fetchAction.objectId,
            pageToken = fetchAction.pageToken,
        )
    ) {
        getPlaylistItems(
            playlistId = it.objectId,
            pageToken = it.pageToken,
        ).apply {
            it.itemCount = items.orEmpty().size
        }
    }

    fun getVideos(fetchActionId: Long, videoIds: List<String>) = callYouTube(
        YouTubeRequestEntity(
            fetchActionId = fetchActionId,
            requestType = RequestType.VIDEOS,
            objectId = videoIds.distinct().joinToString(separator = ","),
        )
    ) {
        getVideos(
            videoIds = videoIds.distinct(),
        ).apply {
            it.itemCount = items.orEmpty().size
        }
    }

    fun getComments(fetchAction: FetchActionEntity) = callYouTube(
        YouTubeRequestEntity(
            fetchActionId = fetchAction.id,
            requestType = RequestType.COMMENTS,
            objectId = fetchAction.objectId,
            pageToken = fetchAction.pageToken,
        )
    ) {
        getComments(
            videoId = it.objectId,
            pageToken = it.pageToken,
        ).apply {
            it.itemCount = items.orEmpty().size
        }
    }

    fun getReplies(fetchAction: FetchActionEntity) = callYouTube(
        YouTubeRequestEntity(
            fetchActionId = fetchAction.id,
            requestType = RequestType.REPLIES,
            objectId = fetchAction.objectId,
            pageToken = fetchAction.pageToken,
        )
    ) {
        getReplies(
            commentId = it.objectId,
            pageToken = it.pageToken,
        ).apply {
            it.itemCount = items.orEmpty().size
        }
    }

    private fun <T> callYouTube(
        request: YouTubeRequestEntity,
        invoke: YouTubeClient.(YouTubeRequestEntity) -> T,
    ): T {
        val youTubeRequest = youTubeRequestRepository.save(request)
        try {
            val response = invoke(youTubeClient, youTubeRequest)
            youTubeRequest.httpStatus = HttpStatus.OK.value()
            return response
        } catch (ex: YouTubeException) {
            youTubeRequest.httpStatus = ex.status.value()
            youTubeRequest.error = ex.stackTraceToString()
            throw ex
        } finally {
            youTubeRequest.respondedAt = OffsetDateTime.now()
            youTubeRequestRepository.save(youTubeRequest)
        }
    }

    private fun OffsetDateTime?.toIsoInstantString() = when (this) {
        null -> null
        else -> DateTimeFormatter.ISO_INSTANT.format(this)
    }
}
