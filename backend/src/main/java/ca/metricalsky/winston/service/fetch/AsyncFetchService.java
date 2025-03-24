package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.dto.fetch.FetchRequest;
import ca.metricalsky.winston.events.FetchChannelEvent;
import ca.metricalsky.winston.events.FetchStatus;
import ca.metricalsky.winston.mapper.dto.ChannelDtoMapper;
import ca.metricalsky.winston.repository.ChannelRepository;
import ca.metricalsky.winston.utils.SseEvent;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AsyncFetchService {

    private final ChannelDtoMapper channelDtoMapper = Mappers.getMapper(ChannelDtoMapper.class);
    private final ChannelRepository channelRepository;
    private final YouTubeService youTubeService;

    @Async
    public void fetchChannel(FetchRequest request, SseEmitter sseEmitter) throws IOException {

        var handle = request.getChannel().getHandle();

        var channel = youTubeService.fetchChannel("@" + handle);
        channelRepository.save(channel);

        var channelDto = channelDtoMapper.fromEntity(channel);
        var eventData = new FetchChannelEvent(handle, FetchStatus.COMPLETED, channelDto);
        sseEmitter.send(SseEvent.named("fetch-channel", eventData));

        sseEmitter.complete();
    }
}
