package ca.metricalsky.yt.comments.web.fetch;

import ca.metricalsky.yt.comments.service.ChannelService;
import ca.metricalsky.yt.comments.service.NotificationsService;
import ca.metricalsky.yt.comments.service.fetch.FetchVideosService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class FetchVideosController {

    private final FetchVideosService fetchVideosService;
    private final ChannelService channelService;
    private final NotificationsService notificationsService;

    @PostMapping("/api/channels/{channelId}/fetch-videos")
    public ResponseEntity<Void> fetchVideosForChannel(
            @PathVariable String channelId,
            @RequestHeader("X-Notify-Subscription") UUID subscriptionId
    ) {
        channelService.requireChannelExists(channelId);
        var sseEmitter = notificationsService.requireSubscription(subscriptionId);
        fetchVideosService.fetchVideosForChannel(channelId, sseEmitter);
        return ResponseEntity.accepted().build();
    }
}
