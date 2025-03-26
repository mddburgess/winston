package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.dto.VideoDto;
import ca.metricalsky.winston.dto.fetch.FetchRequest;
import ca.metricalsky.winston.dto.fetch.FetchVideos;
import ca.metricalsky.winston.events.FetchStatus;
import ca.metricalsky.winston.events.FetchVideosEvent;
import ca.metricalsky.winston.mapper.dto.VideoDtoMapper;
import ca.metricalsky.winston.repository.VideoRepository;
import ca.metricalsky.winston.service.ChannelService;
import ca.metricalsky.winston.utils.SseEvent;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class FetchVideosService implements FetchRequestHandler {

    private final VideoDtoMapper videoDtoMapper = Mappers.getMapper(VideoDtoMapper.class);
    private final YouTubeService youTubeService;
    private final VideoRepository videoRepository;
    private final ChannelService channelService;
    private final FetchRequestService fetchRequestService;
    private final FetchOperationService fetchOperationService;

    @Override
    public void fetch(FetchRequest fetchRequest, SseEmitter sseEmitter) {
        var context = buildFetchContext(fetchRequest);

        try {
            fetchRequestService.startFetch(context);
            do {
                var eventData = fetchVideos(context);
                sseEmitter.send(SseEvent.named("fetch-videos", eventData));
            } while (context.hasNext());
            fetchRequestService.completeFetch(context);
        } catch (Exception ex) {
            fetchRequestService.failFetch(context, ex);
            throw new RuntimeException(ex);
        }
    }

    public FetchVideosContext buildFetchContext(FetchRequest fetchRequest) {
        var channelId = fetchRequest.getVideos().getChannelId();
        var fetchMode = fetchRequest.getVideos().getFetch();

        channelService.requireChannelExists(channelId);
        var lastPublishedAt = fetchMode == FetchVideos.Mode.ALL ? null
                : videoRepository.findLastPublishedAtForChannelId(channelId)
                        .map(date -> date.plusSeconds(1))
                        .orElse(null);

        return FetchVideosContext.builder()
                .channelId(channelId)
                .publishedAfter(lastPublishedAt)
                .build();
    }

    private FetchVideosEvent fetchVideos(FetchVideosContext context) throws IOException {
        try {
            fetchOperationService.startOperation(context);

            var channelId = context.getChannelId();
            var publishedAfter = context.getPublishedAfter();
            var publishedBefore = context.getPublishedBefore();

            var fetchVideosResponse = youTubeService.fetchVideos(channelId, publishedAfter, publishedBefore);
            videoRepository.saveAll(fetchVideosResponse.videos());

            var videoDtos = fetchVideosResponse.videos().stream()
                    .map(videoDtoMapper::fromEntity)
                    .toList();

            context.setPublishedBefore(fetchVideosResponse.nextPublishedBefore());

            var eventStatus = context.hasNext() ? FetchStatus.FETCHING : FetchStatus.COMPLETED;
            var event = new FetchVideosEvent(channelId, eventStatus, videoDtos);

            fetchOperationService.completeOperation(context);
            return event;
        } catch (Exception ex) {
            fetchOperationService.failOperation(context, ex);
            throw ex;
        }
    }
}
