package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.client.YouTubeClient;
import ca.metricalsky.winston.dto.FetchVideosResponse;
import ca.metricalsky.winston.entity.Channel;
import ca.metricalsky.winston.entity.Video;
import ca.metricalsky.winston.mapper.entity.ChannelMapper;
import ca.metricalsky.winston.mapper.entity.OffsetDateTimeMapper;
import ca.metricalsky.winston.mapper.entity.VideoMapper;
import com.google.api.services.youtube.model.Activity;
import com.google.api.services.youtube.model.ActivitySnippet;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class YouTubeService {

    private final ChannelMapper channelMapper = Mappers.getMapper(ChannelMapper.class);
    private final VideoMapper videoMapper = Mappers.getMapper(VideoMapper.class);
    private final OffsetDateTimeMapper offsetDateTimeMapper = new OffsetDateTimeMapper();
    private final YouTubeClient youTubeClient;

    public Channel fetchChannel(String channelHandle) throws IOException {
        var channelListResponse = youTubeClient.getChannel(channelHandle);
        if (channelListResponse.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return channelMapper.fromYouTube(channelListResponse.getItems().getFirst());
    }

    public FetchVideosResponse fetchVideos(
            String channelId, OffsetDateTime publishedAfter, OffsetDateTime publishedBefore)
            throws IOException {

        var activityListResponse = youTubeClient.getActivities(channelId, publishedAfter, publishedBefore);
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
}
