package ca.metricalsky.yt.comments.service.fetch;

import ca.metricalsky.yt.comments.events.FetchStatus;
import ca.metricalsky.yt.comments.events.FetchVideosEvent;
import ca.metricalsky.yt.comments.mapper.dto.VideoDtoMapper;
import ca.metricalsky.yt.comments.repository.VideoRepository;
import ca.metricalsky.yt.comments.service.ChannelService;
import ca.metricalsky.yt.comments.utils.SseEvent;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FetchVideosService {

    private final VideoDtoMapper videoDtoMapper = Mappers.getMapper(VideoDtoMapper.class);
    private final YouTubeService youTubeService;
    private final VideoRepository videoRepository;
    private final ChannelService channelService;

    public FetchVideosContext buildFetchContext(String channelId) {
        channelService.requireChannelExists(channelId);
        var lastPublishedAt = videoRepository.getLastPublishedAtForChannelId(channelId).plusSeconds(1);

        return FetchVideosContext.builder()
                .channelId(channelId)
                .lastPublishedAt(lastPublishedAt)
                .build();
    }

    @Async
    public void asyncFetchVideosForChannel(FetchVideosContext context, SseEmitter sseEmitter) {
        try {
            do {
                var eventData = fetchVideos(context);
                sseEmitter.send(SseEvent.named("fetch-videos", eventData));
                Thread.sleep(1000);
            } while (context.hasNext());
            sseEmitter.complete();
        } catch (Exception ex) {
            sseEmitter.completeWithError(ex);
        }
    }

    private FetchVideosEvent fetchVideos(FetchVideosContext context) throws IOException {
        var channelId = context.getChannelId();
        var lastPublishedAt = context.getLastPublishedAt();
        var nextPageToken = context.getNextPageToken();

        var fetchVideosResponse = youTubeService.fetchVideos(channelId, lastPublishedAt, nextPageToken);
        var videos = fetchVideosResponse.videos().stream()
                .map(videoDtoMapper::fromEntity)
                .toList();
        context.setNextPageToken(fetchVideosResponse.nextPageToken());

        var eventStatus = context.hasNext() ? FetchStatus.FETCHING : FetchStatus.COMPLETED;
        return new FetchVideosEvent(channelId, eventStatus, videos);
    }
}
