package ca.metricalsky.winston.service;

import ca.metricalsky.winston.dto.ChannelDto;
import ca.metricalsky.winston.entity.ChannelEntity;
import ca.metricalsky.winston.exception.AppException;
import ca.metricalsky.winston.mapper.dto.ChannelDtoMapper;
import ca.metricalsky.winston.repository.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChannelService {

    private final ChannelDtoMapper channelDtoMapper = Mappers.getMapper(ChannelDtoMapper.class);
    private final ChannelRepository channelRepository;
    private final VideoService videoService;

    public List<ChannelDto> findAll() {
        var channels = channelRepository.findAll();
        return populateVideoCounts(channels);
    }

    public List<ChannelDto> findAllById(Iterable<String> channelIds) {
        var channels = channelRepository.findAllById(channelIds);
        return populateVideoCounts(channels);
    }

    private List<ChannelDto> populateVideoCounts(List<ChannelEntity> channels) {
        var videoCounts = videoService.countAllByChannelId();
        return channels.stream()
                .map(channelDtoMapper::fromEntity)
                .peek(channelDto -> channelDto.setVideoCount(videoCounts.get(channelDto.getId())))
                .sorted(Comparator.comparing(ChannelDto::getTitle))
                .toList();
    }

    public Optional<ChannelDto> findById(String channelId) {
        return channelRepository.findById(channelId)
                .map(channelDtoMapper::fromEntity);
    }

    public Optional<ChannelDto> findByHandle(String channelHandle) {
        return channelRepository.findByCustomUrl(channelHandle)
                .map(channelDtoMapper::fromEntity);
    }

    public void requireChannelExists(String channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new AppException(HttpStatus.NOT_FOUND, "The requested channel was not found.");
        };
    }
}
