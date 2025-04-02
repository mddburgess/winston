package ca.metricalsky.winston.client;

import ca.metricalsky.winston.entity.fetch.YouTubeRequest;
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

@Service
@RequiredArgsConstructor
public class YouTubeClientAdapter {

    private final YouTubeClient youTubeClient;
    private final YouTubeRequestRepository youTubeRequestRepository;

    public ChannelListResponse getChannels(YouTubeRequest youTubeRequest) {
        youTubeRequest.setRequestedAt(OffsetDateTime.now());
        youTubeRequest = youTubeRequestRepository.save(youTubeRequest);

        try {
            var handle = youTubeRequest.getObjectId();

            var response = youTubeClient.getChannel(handle);

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

    public ActivityListResponse getActivities(YouTubeRequest youTubeRequest) {
        youTubeRequest.setRequestedAt(OffsetDateTime.now());
        youTubeRequest = youTubeRequestRepository.save(youTubeRequest);

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

    public CommentThreadListResponse getComments(YouTubeRequest youTubeRequest) {
        youTubeRequest.setRequestedAt(OffsetDateTime.now());
        youTubeRequest = youTubeRequestRepository.save(youTubeRequest);

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

    public CommentListResponse getReplies(YouTubeRequest youTubeRequest) {
        youTubeRequest.setRequestedAt(OffsetDateTime.now());
        youTubeRequest = youTubeRequestRepository.save(youTubeRequest);

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
}
