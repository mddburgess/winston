package ca.metricalsky.winston.web;

import ca.metricalsky.winston.dto.VideoDto;
import ca.metricalsky.winston.service.ChannelService;
import ca.metricalsky.winston.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class VideoController {

    private final VideoService videoService;
    private final ChannelService channelService;

    @Autowired
    public VideoController(VideoService videoService, ChannelService channelService) {
        this.videoService = videoService;
        this.channelService = channelService;
    }

    @GetMapping("/api/channels/{channelId}/videos")
    public List<VideoDto> listByChannelId(@PathVariable String channelId) {
        return videoService.findAllByChannelId(channelId);
    }

    @GetMapping("/api/videos/{videoId}")
    public VideoDto findById(@PathVariable String videoId) {
        var video = videoService.getById(videoId);
        channelService.findById(video.getChannelId()).ifPresent(channel -> {
            video.setChannel(channel);
            video.setChannelId(null);
        });
        return video;
    }
}
