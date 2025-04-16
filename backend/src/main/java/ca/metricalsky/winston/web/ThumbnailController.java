package ca.metricalsky.winston.web;

import ca.metricalsky.winston.service.ThumbnailService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ThumbnailController {

    private final ThumbnailService thumbnailService;

    public ThumbnailController(ThumbnailService thumbnailService) {
        this.thumbnailService = thumbnailService;
    }

    @GetMapping(path = "/api/channels/{channelId}/thumbnail", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getChannelThumbnail(@PathVariable String channelId) {
        return thumbnailService.getChannelThumbnail(channelId);
    }

    @GetMapping(path = "/api/videos/{videoId}/thumbnail", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getVideoThumbnail(@PathVariable String videoId) {
        return thumbnailService.getVideoThumbnail(videoId);
    }

    @GetMapping(path = "/api/authors/{authorId}/thumbnail", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getAuthorThumbnail(@PathVariable String authorId) {
        return thumbnailService.getAuthorThumbnail(authorId);
    }
}
