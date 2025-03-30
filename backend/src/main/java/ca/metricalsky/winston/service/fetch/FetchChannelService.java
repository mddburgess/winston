package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.dto.fetch.FetchRequestDto;
import ca.metricalsky.winston.entity.fetch.FetchAction;
import ca.metricalsky.winston.entity.fetch.FetchAction.ActionType;
import ca.metricalsky.winston.entity.fetch.FetchRequest;
import ca.metricalsky.winston.events.FetchChannelEvent;
import ca.metricalsky.winston.events.FetchStatus;
import ca.metricalsky.winston.mapper.dto.ChannelDtoMapper;
import ca.metricalsky.winston.repository.ChannelRepository;
import ca.metricalsky.winston.utils.SseEvent;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FetchChannelService implements FetchRequestHandler {

    private final ChannelDtoMapper channelDtoMapper = Mappers.getMapper(ChannelDtoMapper.class);
    private final ChannelRepository channelRepository;
    private final FetchRequestService fetchRequestService;
    private final YouTubeService youTubeService;

    @Override
    public void fetch(FetchRequest fetchRequest, SseEmitter sseEmitter) {
        try {
            fetchRequest = fetchRequestService.startFetch(fetchRequest);

            var fetchAction = new FetchAction();
            fetchAction.setFetchRequestId(fetchRequest.getId());
            fetchAction.setActionType(ActionType.CHANNELS);
            fetchAction.setObjectId("@" + fetchRequest.getObjectId());

            var channel = youTubeService.fetchChannel(fetchAction);
            channelRepository.save(channel);

            var channelDto = channelDtoMapper.fromEntity(channel);
            var eventData = new FetchChannelEvent(fetchRequest.getObjectId(), FetchStatus.COMPLETED, channelDto);
            sseEmitter.send(SseEvent.named("fetch-channel", eventData));

            fetchRequestService.fetchCompleted(fetchRequest);
        } catch (IOException ex) {
            fetchRequestService.fetchFailed(fetchRequest, ex);
            throw new RuntimeException(ex);
        }
    }
}
