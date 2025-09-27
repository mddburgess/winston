package ca.metricalsky.winston.web;

import ca.metricalsky.winston.api.VideosApi;
import ca.metricalsky.winston.api.model.ListVideosResponse;
import ca.metricalsky.winston.api.model.ListVideosResponseResults;
import ca.metricalsky.winston.api.model.Video;
import ca.metricalsky.winston.config.api.VideosApiConfig;
import ca.metricalsky.winston.dao.ChannelDataService;
import ca.metricalsky.winston.dao.VideoDataService;
import ca.metricalsky.winston.exception.AppException;
import ca.metricalsky.winston.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import static com.google.common.base.MoreObjects.firstNonNull;

@RestController
@RequiredArgsConstructor
public class VideoController implements VideosApi {

    private final ChannelDataService channelDataService;
    private final VideoDataService videoDataService;
    private final VideosApiConfig config;

    @Override
    public ResponseEntity<ListVideosResponse> listVideos(String handle, Integer page, Integer size) {
        var queryPage = firstNonNull(page, 0);
        var querySize = firstNonNull(size, config.getDefaultPageSize());

        var channelId = channelDataService.findChannelIdByHandle(handle)
                .orElseThrow(() -> new AppException(ErrorCode.CHANNEL_NOT_FOUND));

        var totalCount = videoDataService.countByChannelId(channelId);
        var videos = videoDataService.getVideosByChannelId(channelId, queryPage, querySize);

        var response = new ListVideosResponse()
                .channelHandle(handle)
                .results(new ListVideosResponseResults()
                        .pageCount(videos.size())
                        .totalCount(totalCount))
                .videos(videos);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Video> getVideo(String id) {
        var video = videoDataService.findVideoById(id)
                .orElseThrow(() -> new AppException(ErrorCode.VIDEO_NOT_FOUND));

        return ResponseEntity.ok(video);
    }
}
