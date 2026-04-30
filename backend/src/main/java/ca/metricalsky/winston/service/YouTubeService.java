package ca.metricalsky.winston.service;

import ca.metricalsky.winston.client.YouTubeClient;
import ca.metricalsky.winston.client.YouTubeException;
import ca.metricalsky.winston.database.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.database.entity.fetch.YouTubeRequestEntity;
import ca.metricalsky.winston.database.repository.fetch.YouTubeRequestRepository;
import com.google.api.services.youtube.model.ActivityListResponse;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.CommentListResponse;
import com.google.api.services.youtube.model.CommentThreadListResponse;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.VideoListResponse;
import com.google.common.base.Throwables;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.google.common.base.MoreObjects.firstNonNull;

@Service
@RequiredArgsConstructor
public class YouTubeService {

    private final YouTubeClient youTubeClient;
    private final YouTubeRequestRepository youTubeRequestRepository;

    public ChannelListResponse getChannels(FetchActionEntity fetchAction) {
        var entity = new YouTubeRequestEntity();
        entity.setFetchActionId(fetchAction.getId());
        entity.setRequestType(YouTubeRequestEntity.RequestType.CHANNELS);
        entity.setObjectId(fetchAction.getObjectId());
        entity.setRequestedAt(OffsetDateTime.now());
        var youTubeRequest = youTubeRequestRepository.save(entity);

        try {
            var handle = youTubeRequest.getObjectId();

            var response = youTubeClient.getChannel(handle);

            youTubeRequest.setHttpStatus(HttpStatus.OK.value());
            youTubeRequest.setItemCount(firstNonNull(response.getItems(), List.of()).size());
            return response;
        } catch (YouTubeException ex) {
            youTubeRequest.setHttpStatus(ex.getStatusCode().value());
            youTubeRequest.setError(Throwables.getStackTraceAsString(ex));
            throw ex;
        } finally {
            youTubeRequest.setRespondedAt(OffsetDateTime.now());
            youTubeRequestRepository.save(youTubeRequest);
        }
    }

    public ActivityListResponse getActivities(FetchActionEntity fetchAction) {
        var entity = new YouTubeRequestEntity();
        entity.setFetchActionId(fetchAction.getId());
        entity.setRequestType(YouTubeRequestEntity.RequestType.ACTIVITIES);
        entity.setObjectId(fetchAction.getObjectId());
        entity.setPublishedAfter(formatDate(fetchAction.getPublishedAfter()));
        entity.setPublishedBefore(formatDate(fetchAction.getPublishedBefore()));
        entity.setRequestedAt(OffsetDateTime.now());
        var youTubeRequest = youTubeRequestRepository.save(entity);

        try {
            var channelId = youTubeRequest.getObjectId();
            var publishedAfter = youTubeRequest.getPublishedAfter();
            var publishedBefore = youTubeRequest.getPublishedBefore();

            var response = youTubeClient.getActivities(channelId, publishedAfter, publishedBefore);

            youTubeRequest.setHttpStatus(HttpStatus.OK.value());
            youTubeRequest.setItemCount(response.getItems().size());
            return response;
        } catch (YouTubeException ex) {
            youTubeRequest.setHttpStatus(ex.getStatusCode().value());
            youTubeRequest.setError(Throwables.getStackTraceAsString(ex));
            throw ex;
        } finally {
            youTubeRequest.setRespondedAt(OffsetDateTime.now());
            youTubeRequestRepository.save(youTubeRequest);
        }
    }

    public PlaylistItemListResponse getPlaylistItems(FetchActionEntity fetchAction) {
        var entity = new YouTubeRequestEntity();
        entity.setFetchActionId(fetchAction.getId());
        entity.setRequestType(YouTubeRequestEntity.RequestType.PLAYLIST_ITEMS);
        entity.setObjectId(fetchAction.getObjectId());
        entity.setPageToken(fetchAction.getPageToken());
        entity.setRequestedAt(OffsetDateTime.now());
        var youTubeRequest = youTubeRequestRepository.save(entity);

        try {
            var playlistId =  youTubeRequest.getObjectId();
            var pageToken = youTubeRequest.getPageToken();

            var response = youTubeClient.getPlaylistItems(playlistId, pageToken);

            youTubeRequest.setHttpStatus(HttpStatus.OK.value());
            youTubeRequest.setItemCount(response.getItems().size());
            return response;
        } catch (YouTubeException ex) {
            youTubeRequest.setHttpStatus(ex.getStatusCode().value());
            youTubeRequest.setError(Throwables.getStackTraceAsString(ex));
            throw ex;
        } finally {
            youTubeRequest.setRespondedAt(OffsetDateTime.now());
            youTubeRequestRepository.save(youTubeRequest);
        }
    }

    public VideoListResponse getVideos(Long fetchActionId, List<String> videoIds) {
        var distinctVideoIds = videoIds.stream().distinct().toList();

        var entity = new YouTubeRequestEntity();
        entity.setFetchActionId(fetchActionId);
        entity.setRequestType(YouTubeRequestEntity.RequestType.VIDEOS);
        entity.setObjectId(String.join(",", distinctVideoIds));
        entity.setRequestedAt(OffsetDateTime.now());
        var youTubeRequest = youTubeRequestRepository.save(entity);

        try {
            var response = youTubeClient.getVideos(distinctVideoIds);

            youTubeRequest.setHttpStatus(HttpStatus.OK.value());
            youTubeRequest.setItemCount(response.getItems().size());
            return response;
        } catch (YouTubeException ex) {
            youTubeRequest.setHttpStatus(ex.getStatusCode().value());
            youTubeRequest.setError(Throwables.getStackTraceAsString(ex));
            throw ex;
        } finally {
            youTubeRequest.setRespondedAt(OffsetDateTime.now());
            youTubeRequestRepository.save(youTubeRequest);
        }
    }

    public CommentThreadListResponse getComments(FetchActionEntity fetchAction) {
        var entity = new YouTubeRequestEntity();
        entity.setFetchActionId(fetchAction.getId());
        entity.setRequestType(YouTubeRequestEntity.RequestType.COMMENTS);
        entity.setObjectId(fetchAction.getObjectId());
        entity.setPageToken(fetchAction.getPageToken());
        entity.setRequestedAt(OffsetDateTime.now());
        var youTubeRequest = youTubeRequestRepository.save(entity);

        try {
            var videoId = youTubeRequest.getObjectId();
            var pageToken = youTubeRequest.getPageToken();

            var response = youTubeClient.getComments(videoId, pageToken);

            youTubeRequest.setHttpStatus(HttpStatus.OK.value());
            youTubeRequest.setItemCount(response.getItems().size());
            return response;
        } catch (YouTubeException ex) {
            youTubeRequest.setHttpStatus(ex.getStatusCode().value());
            youTubeRequest.setError(Throwables.getStackTraceAsString(ex));
            throw ex;
        } finally {
            youTubeRequest.setRespondedAt(OffsetDateTime.now());
            youTubeRequestRepository.save(youTubeRequest);
        }
    }

    public CommentListResponse getReplies(FetchActionEntity fetchAction) {
        var entity = new YouTubeRequestEntity();
        entity.setFetchActionId(fetchAction.getId());
        entity.setRequestType(YouTubeRequestEntity.RequestType.REPLIES);
        entity.setObjectId(fetchAction.getObjectId());
        entity.setPageToken(fetchAction.getPageToken());
        entity.setRequestedAt(OffsetDateTime.now());
        var youTubeRequest = youTubeRequestRepository.save(entity);

        try {
            var commentId = youTubeRequest.getObjectId();
            var pageToken = youTubeRequest.getPageToken();

            var response = youTubeClient.getReplies(commentId, pageToken);

            youTubeRequest.setHttpStatus(HttpStatus.OK.value());
            youTubeRequest.setItemCount(response.getItems().size());
            return response;
        } catch (YouTubeException ex) {
            youTubeRequest.setHttpStatus(ex.getStatusCode().value());
            youTubeRequest.setError(Throwables.getStackTraceAsString(ex));
            throw ex;
        } finally {
            youTubeRequest.setRespondedAt(OffsetDateTime.now());
            youTubeRequestRepository.save(youTubeRequest);
        }
    }

    private static String formatDate(OffsetDateTime date) {
        return date != null ? DateTimeFormatter.ISO_INSTANT.format(date) : null;
    }
}
