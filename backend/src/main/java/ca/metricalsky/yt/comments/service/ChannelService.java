package ca.metricalsky.yt.comments.service;

import ca.metricalsky.yt.comments.dto.ChannelDto;
import ca.metricalsky.yt.comments.mapper.dto.ChannelDtoMapper;
import ca.metricalsky.yt.comments.repository.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChannelService {

    private final ChannelDtoMapper channelDtoMapper = Mappers.getMapper(ChannelDtoMapper.class);
    private final ChannelRepository channelRepository;
    private final VideoService videoService;

    @Transactional(readOnly = true)
    public List<ChannelDto> findAll() {
        var videoCounts = videoService.countAllByChannelId();
        return channelRepository.findAll()
                .stream()
                .map(channelDtoMapper::fromEntity)
                .peek(channelDto -> channelDto.setVideoCount(videoCounts.get(channelDto.getId())))
                .sorted(Comparator.comparing(ChannelDto::getTitle))
                .toList();
    }

    public Optional<ChannelDto> findById(String channelId) {
        return channelRepository.findById(channelId)
                .map(channelDtoMapper::fromEntity);
    }

    public void requireChannelExists(String channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The requested channel was not found.");
        };
    }
}
