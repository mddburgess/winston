package ca.metricalsky.yt.comments.service.fetch;

import ca.metricalsky.yt.comments.dto.VideoDto;
import ca.metricalsky.yt.comments.events.FetchStatus;
import ca.metricalsky.yt.comments.events.FetchVideosEvent;
import ca.metricalsky.yt.comments.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FetchVideosService {

    private final VideoService videoService;

    @Async
    public void fetchVideosForChannel(String channelId, SseEmitter sseEmitter) {
        try {
            var page = PageRequest.of(0, 10);
            List<VideoDto> videos;

            do {
                videos = videoService.findByChannelId(channelId, page);

                var eventStatus = videos.isEmpty() ? FetchStatus.COMPLETED : FetchStatus.FETCHING;
                var eventData = new FetchVideosEvent(channelId, eventStatus, videos);
                var event = SseEmitter.event()
                        .id(String.valueOf(page.getPageNumber()))
                        .name("fetch-videos")
                        .data(eventData, MediaType.APPLICATION_JSON);

                sseEmitter.send(event);
                page = page.next();
            } while (!videos.isEmpty());

            sseEmitter.complete();
        } catch (Exception ex) {
            sseEmitter.completeWithError(ex);
        }
    }
}
