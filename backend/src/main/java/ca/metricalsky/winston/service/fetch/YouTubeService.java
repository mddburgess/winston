package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.client.YouTubeClient;
import ca.metricalsky.winston.dto.FetchVideosResponse;
import ca.metricalsky.winston.mapper.entity.VideoMapper;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class YouTubeService {

    private final VideoMapper videoMapper = Mappers.getMapper(VideoMapper.class);
    private final YouTubeClient youTubeClient;

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
