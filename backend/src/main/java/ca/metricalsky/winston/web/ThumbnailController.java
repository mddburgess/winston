package ca.metricalsky.winston.web;

import ca.metricalsky.winston.api.ThumbnailsApi;
import ca.metricalsky.winston.service.ThumbnailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ThumbnailController implements ThumbnailsApi {

    private final ThumbnailService thumbnailService;

    @Override
    public ResponseEntity<byte[]> getChannelThumbnail(String channelId) {
        var thumbnail = thumbnailService.getChannelThumbnail(channelId);
        return ResponseEntity.ok(thumbnail);
    }

    @Override
    public ResponseEntity<byte[]> getVideoThumbnail(String videoId) {
        var thumbnail = thumbnailService.getVideoThumbnail(videoId);
        return ResponseEntity.ok(thumbnail);
    }

    @Override
    public ResponseEntity<byte[]> getAuthorThumbnail(String authorId) {
        var thumbnail = thumbnailService.getAuthorThumbnail(authorId);
        return ResponseEntity.ok(thumbnail);
    }
}
