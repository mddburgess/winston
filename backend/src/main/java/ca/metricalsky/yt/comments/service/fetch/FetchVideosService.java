package ca.metricalsky.yt.comments.service.fetch;

import ca.metricalsky.yt.comments.events.FetchStatus;
import ca.metricalsky.yt.comments.events.FetchVideosEvent;
import ca.metricalsky.yt.comments.mapper.dto.VideoDtoMapper;
import ca.metricalsky.yt.comments.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FetchVideosService {

    private final VideoDtoMapper videoDtoMapper = Mappers.getMapper(VideoDtoMapper.class);
    private final YouTubeService youTubeService;
    private final VideoRepository videoRepository;

    @Async
    public void fetchVideosForChannel(String channelId, SseEmitter sseEmitter) throws IOException {
        try {
            var lastPublishedAt = videoRepository.getLastPublishedAtForChannelId(channelId).plusSeconds(1);
            String nextPageToken = null;

            do {
                var fetchVideosResponse = youTubeService.fetchVideos(channelId, lastPublishedAt, nextPageToken);
                var videos = fetchVideosResponse.videos().stream()
                        .map(videoDtoMapper::fromEntity)
                        .toList();
                nextPageToken = fetchVideosResponse.nextPageToken();

                var eventStatus = nextPageToken == null ? FetchStatus.COMPLETED : FetchStatus.FETCHING;
                var eventData = new FetchVideosEvent(channelId, eventStatus, videos);
                var event = SseEmitter.event()
                        .id(UUID.randomUUID().toString())
                        .name("fetch-videos")
                        .data(eventData, MediaType.APPLICATION_JSON);

                sseEmitter.send(event);
                Thread.sleep(1000);
            } while (nextPageToken != null);

            sseEmitter.complete();
        } catch (Exception ex) {
            sseEmitter.completeWithError(ex);
        }
    }
}
