package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.client.YouTubeClientAdapter;
import ca.metricalsky.winston.dto.ChannelDto;
import ca.metricalsky.winston.entity.fetch.FetchAction;
import ca.metricalsky.winston.mapper.dto.ChannelDtoMapper;
import ca.metricalsky.winston.mapper.entity.ChannelMapper;
import ca.metricalsky.winston.repository.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FetchChannelActionHandler implements FetchActionHandler<ChannelDto> {

    private final ChannelMapper channelMapper = Mappers.getMapper(ChannelMapper.class);
    private final ChannelDtoMapper channelDtoMapper = Mappers.getMapper(ChannelDtoMapper.class);
    private final ChannelRepository channelRepository;
    private final YouTubeClientAdapter youTubeClientAdapter;

    @Override
    public FetchResult<ChannelDto> fetch(FetchAction fetchAction) {
        var channelListResponse = youTubeClientAdapter.getChannels(fetchAction);
        var channelEntity = Optional.ofNullable(channelListResponse.getItems())
                .orElse(Collections.emptyList())
                .stream()
                .findFirst()
                .map(channelMapper::fromYouTube)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        channelRepository.save(channelEntity);
        var channelDto = channelDtoMapper.fromEntity(channelEntity);
        return new FetchResult<>(fetchAction, channelDto, null);
    }
}
