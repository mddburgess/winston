package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.dto.fetch.FetchRequestDto;
import ca.metricalsky.winston.dto.fetch.FetchVideos;
import ca.metricalsky.winston.entity.fetch.FetchAction.ActionType;
import ca.metricalsky.winston.entity.fetch.FetchRequest;
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

@Service
@RequiredArgsConstructor
public class FetchVideosService implements FetchRequestHandler {

    private final VideoDtoMapper videoDtoMapper = Mappers.getMapper(VideoDtoMapper.class);
    private final YouTubeService youTubeService;
    private final VideoRepository videoRepository;
    private final ChannelService channelService;
    private final FetchActionService fetchActionService;
    private final FetchRequestService fetchRequestService;

    @Override
    public void fetch(FetchRequest fetchRequest, SseEmitter sseEmitter) {
        try {
            var fetchContext = buildFetchContext(fetchRequest);
            fetchRequest = fetchRequestService.startFetch(fetchRequest);

            do {
                var eventData = fetchVideos(fetchContext);
                sseEmitter.send(SseEvent.named("fetch-videos", eventData));
            } while (fetchContext.getPublishedBefore() != null);

            fetchRequestService.fetchCompleted(fetchRequest);
        } catch (Exception ex) {
            fetchRequestService.fetchFailed(fetchRequest, ex);
            throw new RuntimeException(ex);
        }
    }

    public FetchContext buildFetchContext(FetchRequest fetchRequest) {
        var channelId = fetchRequest.getObjectId();
        var fetchMode = FetchVideos.Mode.valueOf(fetchRequest.getMode());

        channelService.requireChannelExists(channelId);
        var publishedAfter = fetchMode == FetchVideos.Mode.ALL ? null
                : videoRepository.findLastPublishedAtForChannelId(channelId)
                        .map(date -> date.plusSeconds(1))
                        .orElse(null);
        fetchRequest.setPublishedAfter(publishedAfter);

        return FetchContext.builder()
                .objectId(channelId)
                .publishedAfter(publishedAfter)
                .fetchRequest(fetchRequest)
                .build();
    }

    private FetchVideosEvent fetchVideos(FetchContext fetchContext) throws IOException {
        var fetchAction = fetchActionService.startAction(fetchContext, ActionType.VIDEOS);
        try {
            var fetchVideosResponse = youTubeService.fetchVideos(fetchAction);
            videoRepository.saveAll(fetchVideosResponse.videos());

            var videoDtos = fetchVideosResponse.videos().stream()
                    .map(videoDtoMapper::fromEntity)
                    .toList();

            fetchContext.setPublishedBefore(fetchVideosResponse.nextPublishedBefore());

            var eventStatus = fetchContext.getPublishedBefore() != null ? FetchStatus.FETCHING : FetchStatus.COMPLETED;
            var event = new FetchVideosEvent(fetchContext.getObjectId(), eventStatus, videoDtos);

            fetchActionService.actionCompleted(fetchAction, videoDtos.size());
            return event;
        } catch (Exception ex) {
            fetchActionService.actionFailed(fetchAction, ex);
            throw ex;
        }
    }
}
