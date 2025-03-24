package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.client.YouTubeClient;
import ca.metricalsky.winston.dto.FetchVideosResponse;
import ca.metricalsky.winston.entity.Channel;
import ca.metricalsky.winston.mapper.entity.ChannelMapper;
import ca.metricalsky.winston.mapper.entity.VideoMapper;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class YouTubeService {

    private final ChannelMapper channelMapper = Mappers.getMapper(ChannelMapper.class);
    private final VideoMapper videoMapper = Mappers.getMapper(VideoMapper.class);
    private final YouTubeClient youTubeClient;

    public Channel fetchChannel(String channelHandle) throws IOException {
        var channelListResponse = youTubeClient.getChannel(channelHandle);
        if (channelListResponse.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return channelMapper.fromYouTube(channelListResponse.getItems().getFirst());
    }

    public FetchVideosResponse fetchVideos(String channelId, OffsetDateTime publishedAt, String pageToken)
            throws IOException {

        var activityListResponse = youTubeClient.getActivities(channelId, publishedAt, pageToken);
        var videos = activityListResponse.getItems()
                .stream()
                .filter(activity -> activity.getContentDetails().getUpload() != null)
                .map(videoMapper::fromYouTube)
                .toList();

        return new FetchVideosResponse(
                activityListResponse.getPageInfo().getResultsPerPage(),
                activityListResponse.getPageInfo().getTotalResults(),
                activityListResponse.getNextPageToken(),
                videos
        );
    }
}
