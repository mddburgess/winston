package ca.metricalsky.yt.comments.web;

import ca.metricalsky.yt.comments.dto.VideoDto;
import ca.metricalsky.yt.comments.service.VideoService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class VideoController {

    private final VideoService videoService;

    @Autowired
    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @GetMapping("/api/channels/{channelId}/videos")
    public List<VideoDto> listByChannelId(@PathVariable String channelId) {
        return videoService.findAllByChannelId(channelId);
    }
}
