package ca.metricalsky.winston.service.fetch.action;

import ca.metricalsky.winston.service.YouTubeService;
import ca.metricalsky.winston.dto.ChannelDto;
import ca.metricalsky.winston.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.exception.AppException;
import ca.metricalsky.winston.mapper.dto.ChannelDtoMapper;
import ca.metricalsky.winston.mapper.entity.ChannelMapper;
import ca.metricalsky.winston.repository.ChannelRepository;
import ca.metricalsky.winston.service.fetch.FetchActionService;
import ca.metricalsky.winston.service.fetch.FetchResult;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class FetchChannelActionHandler extends FetchActionHandler<ChannelDto> {

    private final ChannelMapper channelMapper = Mappers.getMapper(ChannelMapper.class);
    private final ChannelDtoMapper channelDtoMapper = Mappers.getMapper(ChannelDtoMapper.class);

    private final ChannelRepository channelRepository;
    private final YouTubeService youTubeService;

    public FetchChannelActionHandler(
            FetchActionService fetchActionService,
            ChannelRepository channelRepository,
            YouTubeService youTubeService
    ) {
        super(fetchActionService);
        this.channelRepository = channelRepository;
        this.youTubeService = youTubeService;
    }

    @Override
    protected FetchResult<ChannelDto> doFetch(FetchActionEntity fetchAction) {
        var channelListResponse = youTubeService.getChannels(fetchAction);
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
}
