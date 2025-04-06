package ca.metricalsky.winston.service.fetch.action;

import ca.metricalsky.winston.client.YouTubeClientAdapter;
import ca.metricalsky.winston.dto.ChannelDto;
import ca.metricalsky.winston.entity.fetch.FetchAction;
import ca.metricalsky.winston.events.FetchEvent;
import ca.metricalsky.winston.events.FetchStatus;
import ca.metricalsky.winston.exception.AppException;
import ca.metricalsky.winston.mapper.dto.ChannelDtoMapper;
import ca.metricalsky.winston.mapper.entity.ChannelMapper;
import ca.metricalsky.winston.repository.ChannelRepository;
import ca.metricalsky.winston.service.fetch.FetchActionService;
import ca.metricalsky.winston.service.fetch.FetchResult;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Optional;

@Service
public class FetchChannelActionHandler extends FetchActionHandler<ChannelDto> {

    private final ChannelMapper channelMapper = Mappers.getMapper(ChannelMapper.class);
    private final ChannelDtoMapper channelDtoMapper = Mappers.getMapper(ChannelDtoMapper.class);

    private final ChannelRepository channelRepository;
    private final YouTubeClientAdapter youTubeClientAdapter;

    public FetchChannelActionHandler(
            FetchActionService fetchActionService,
            ChannelRepository channelRepository,
            YouTubeClientAdapter youTubeClientAdapter
    ) {
        super(fetchActionService);
        this.channelRepository = channelRepository;
        this.youTubeClientAdapter = youTubeClientAdapter;
    }

    @Override
    protected FetchResult<ChannelDto> doFetch(FetchAction fetchAction) {
        var channelListResponse = youTubeClientAdapter.getChannels(fetchAction);
        var channelEntity = Optional.ofNullable(channelListResponse.getItems())
                .orElse(Collections.emptyList())
                .stream()
                .findFirst()
                .map(channelMapper::fromYouTube)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "The requested channel does not exist."));
        channelRepository.save(channelEntity);
        var channelDto = channelDtoMapper.fromEntity(channelEntity);
        return new FetchResult<>(fetchAction, channelDto, null);
    }

    @Override
    protected FetchEvent getFetchEvent(FetchResult<ChannelDto> fetchResult) {
        var status = fetchResult.hasNextFetchAction() ? FetchStatus.FETCHING : FetchStatus.COMPLETED;
        return FetchEvent.data("fetch-channels", fetchResult.objectId(), status, fetchResult.items());
    }
}
