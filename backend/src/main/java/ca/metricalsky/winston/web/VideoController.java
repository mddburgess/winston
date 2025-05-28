package ca.metricalsky.winston.web;

import ca.metricalsky.winston.api.VideosApi;
import ca.metricalsky.winston.api.model.ListVideosForChannelResponse;
import ca.metricalsky.winston.api.model.Video;
import ca.metricalsky.winston.dao.VideoDataService;
import ca.metricalsky.winston.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VideoController implements VideosApi {

    private final VideoDataService videoDataService;

    @Override
    public ResponseEntity<ListVideosForChannelResponse> listVideosForChannel(String handle) {
        var videos = videoDataService.getVideosForChannel(handle);
        var response = new ListVideosForChannelResponse()
                .channelHandle(handle)
                .videos(videos);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Video> getVideoById(String id) {
        var video = videoDataService.findVideoById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "The requested video was not found."));

        return ResponseEntity.ok(video);
    }
}
