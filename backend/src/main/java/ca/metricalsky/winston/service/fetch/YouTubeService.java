package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.client.YouTubeClient;
import ca.metricalsky.winston.client.YouTubeClientAdapter;
import ca.metricalsky.winston.dto.FetchCommentsResponse;
import ca.metricalsky.winston.dto.FetchVideosResponse;
import ca.metricalsky.winston.entity.Channel;
import ca.metricalsky.winston.entity.Video;
import ca.metricalsky.winston.entity.fetch.FetchAction;
import ca.metricalsky.winston.entity.fetch.FetchAction.Status;
import ca.metricalsky.winston.entity.fetch.YouTubeRequest;
import ca.metricalsky.winston.entity.fetch.YouTubeRequest.RequestType;
import ca.metricalsky.winston.mapper.entity.ChannelMapper;
import ca.metricalsky.winston.mapper.entity.CommentMapper;
import ca.metricalsky.winston.mapper.entity.OffsetDateTimeMapper;
import ca.metricalsky.winston.mapper.entity.VideoMapper;
import ca.metricalsky.winston.repository.fetch.FetchActionRepository;
import com.google.api.services.youtube.model.Activity;
import com.google.api.services.youtube.model.ActivitySnippet;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class YouTubeService {

    private final ChannelMapper channelMapper = Mappers.getMapper(ChannelMapper.class);
    private final VideoMapper videoMapper = Mappers.getMapper(VideoMapper.class);
    private final CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);
    private final OffsetDateTimeMapper offsetDateTimeMapper = new OffsetDateTimeMapper();
    private final FetchActionRepository fetchActionRepository;
    private final YouTubeClientAdapter youTubeClientAdapter;

    public Channel fetchChannel(FetchAction fetchAction) throws IOException {
        fetchAction.setStatus(Status.FETCHING);
        fetchAction = fetchActionRepository.save(fetchAction);

        var youTubeRequest = new YouTubeRequest();
        youTubeRequest.setFetchActionId(fetchAction.getId());
        youTubeRequest.setRequestType(RequestType.CHANNELS);
        youTubeRequest.setObjectId(fetchAction.getObjectId());

        try {
            var channelListResponse = youTubeClientAdapter.getChannels(youTubeRequest);

            var channel = channelListResponse.getItems()
                    .stream()
                    .findFirst()
                    .map(channelMapper::fromYouTube)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

            fetchAction.setStatus(Status.COMPLETED);
            fetchAction.setItemCount(channelListResponse.getItems().size());
            return channel;
        } catch (IOException | RuntimeException ex) {
            fetchAction.setStatus(Status.FAILED);
            fetchAction.setError(ex.getMessage());
            throw ex;
        } finally {
            fetchActionRepository.save(fetchAction);
        }
    }

    public FetchVideosResponse fetchVideos(FetchAction fetchAction) throws IOException {
        var youTubeRequest = new YouTubeRequest();
        youTubeRequest.setFetchActionId(fetchAction.getId());
        youTubeRequest.setRequestType(RequestType.ACTIVITIES);
        youTubeRequest.setObjectId(fetchAction.getObjectId());
        youTubeRequest.setPublishedAfter(fetchAction.getPublishedAfter());
        youTubeRequest.setPublishedBefore(fetchAction.getPublishedBefore());

        var activityListResponse = youTubeClientAdapter.getActivities(youTubeRequest);
        var videos = activityListResponse.getItems()
                .stream()
                .filter(activity -> activity.getContentDetails().getUpload() != null)
                .map(videoMapper::fromYouTube)
                .toList();

        var publishedAts = !videos.isEmpty()
                ? videos.stream()
                        .map(Video::getPublishedAt)
                : activityListResponse.getItems().stream()
                        .map(Activity::getSnippet)
                        .map(ActivitySnippet::getPublishedAt)
                        .map(offsetDateTimeMapper::fromYouTube);
        var nextPublishedBefore = publishedAts.min(Comparator.naturalOrder())
                .map(publishedAt -> publishedAt.minusSeconds(1))
                .orElse(null);

        return new FetchVideosResponse(
                activityListResponse.getPageInfo().getResultsPerPage(),
                activityListResponse.getPageInfo().getTotalResults(),
                activityListResponse.getNextPageToken(),
                nextPublishedBefore,
                videos
        );
    }

    public FetchCommentsResponse fetchComments(FetchAction fetchAction) throws IOException {
        fetchAction.setStatus(Status.FETCHING);
        fetchAction = fetchActionRepository.save(fetchAction);

        var youTubeRequest = new YouTubeRequest();
        youTubeRequest.setFetchActionId(fetchAction.getId());
        youTubeRequest.setRequestType(RequestType.COMMENTS);
        youTubeRequest.setObjectId(fetchAction.getObjectId());
        youTubeRequest.setPageToken(fetchAction.getPageToken());

        try {
            var commentThreadListResponse = youTubeClientAdapter.getComments(youTubeRequest);
            var comments = commentThreadListResponse.getItems()
                    .stream()
                    .map(commentMapper::fromYouTube)
                    .toList();
            var nextPageToken = commentThreadListResponse.getNextPageToken();

            var response = new FetchCommentsResponse(comments, nextPageToken);

            fetchAction.setStatus(Status.COMPLETED);
            fetchAction.setItemCount(commentThreadListResponse.getItems().size());
            return response;
        } catch (IOException | RuntimeException ex) {
            fetchAction.setStatus(Status.FAILED);
            fetchAction.setError(ex.getMessage());
            throw ex;
        } finally {
            fetchActionRepository.save(fetchAction);
        }
    }
}
