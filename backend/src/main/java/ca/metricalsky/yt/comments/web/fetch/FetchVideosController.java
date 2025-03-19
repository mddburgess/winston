package ca.metricalsky.yt.comments.web.fetch;

import ca.metricalsky.yt.comments.service.NotificationsService;
import ca.metricalsky.yt.comments.service.fetch.FetchRequestService;
import ca.metricalsky.yt.comments.service.fetch.FetchVideosService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class FetchVideosController {

    private final FetchVideosService fetchVideosService;
    private final FetchRequestService fetchRequestService;
    private final NotificationsService notificationsService;

    @PostMapping("/api/channels/{channelId}/fetch-videos")
    public ResponseEntity<SseEmitter> fetchVideosForChannel(
            @PathVariable String channelId,
            @RequestHeader(value = "X-Notify-Subscription", required = false) UUID subscriptionId
    ) throws IOException {
        var context = fetchVideosService.buildFetchContext(channelId);
        var sseEmitter = subscriptionId != null
                ? notificationsService.requireSubscription(subscriptionId)
                : notificationsService.openSubscription();

        fetchRequestService.acceptFetch(context);
        fetchVideosService.asyncFetchVideosForChannel(context, sseEmitter);

        return subscriptionId != null
                ? ResponseEntity.accepted().build()
                : ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_EVENT_STREAM).body(sseEmitter);
    }
}
