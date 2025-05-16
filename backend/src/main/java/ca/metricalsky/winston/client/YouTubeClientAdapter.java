package ca.metricalsky.winston.client;

import ca.metricalsky.winston.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.entity.fetch.YouTubeRequestEntity;
import ca.metricalsky.winston.repository.fetch.YouTubeRequestRepository;
import com.google.api.services.youtube.model.ActivityListResponse;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.CommentListResponse;
import com.google.api.services.youtube.model.CommentThreadListResponse;
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
public class YouTubeClientAdapter {

    private final YouTubeClient youTubeClient;
    private final YouTubeRequestRepository youTubeRequestRepository;

    public ChannelListResponse getChannels(FetchActionEntity fetchAction) {
        var youTubeRequest = youTubeRequestRepository.save(YouTubeRequestEntity.builder()
                .fetchActionId(fetchAction.getId())
                .requestType(YouTubeRequestEntity.RequestType.CHANNELS)
                .objectId(fetchAction.getObjectId())
                .requestedAt(OffsetDateTime.now())
                .build());

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
        var youTubeRequest = youTubeRequestRepository.save(YouTubeRequestEntity.builder()
                .fetchActionId(fetchAction.getId())
                .requestType(YouTubeRequestEntity.RequestType.ACTIVITIES)
                .objectId(fetchAction.getObjectId())
                .publishedAfter(formatDate(fetchAction.getPublishedAfter()))
                .publishedBefore(formatDate(fetchAction.getPublishedBefore()))
                .requestedAt(OffsetDateTime.now())
                .build());

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

    public CommentThreadListResponse getComments(FetchActionEntity fetchAction) {
        var youTubeRequest = youTubeRequestRepository.save(YouTubeRequestEntity.builder()
                .fetchActionId(fetchAction.getId())
                .requestType(YouTubeRequestEntity.RequestType.COMMENTS)
                .objectId(fetchAction.getObjectId())
                .pageToken(fetchAction.getPageToken())
                .requestedAt(OffsetDateTime.now())
                .build());

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
        var youTubeRequest = youTubeRequestRepository.save(YouTubeRequestEntity.builder()
                .fetchActionId(fetchAction.getId())
                .requestType(YouTubeRequestEntity.RequestType.REPLIES)
                .objectId(fetchAction.getObjectId())
                .pageToken(fetchAction.getPageToken())
                .requestedAt(OffsetDateTime.now())
                .build());

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
