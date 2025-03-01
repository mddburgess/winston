package ca.metricalsky.yt.comments.service;

import ca.metricalsky.yt.comments.dto.ChannelDto;
import ca.metricalsky.yt.comments.mapper.ChannelMapper;
import ca.metricalsky.yt.comments.repository.ChannelRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class ChannelService {

    private final ChannelMapper channelMapper = Mappers.getMapper(ChannelMapper.class);

    private final ChannelRepository channelRepository;
    private final VideoService videoService;

    @Autowired
    public ChannelService(ChannelRepository channelRepository, VideoService videoService) {
        this.channelRepository = channelRepository;
        this.videoService = videoService;
    }

    @Transactional(readOnly = true)
    public List<ChannelDto> findAll() {
        var videoCounts = videoService.countAllByChannelId();
        return channelRepository.findAll()
                .stream()
                .map(channelMapper::toDto)
                .peek(channelDto -> channelDto.setVideoCount(videoCounts.get(channelDto.getId())))
                .sorted(Comparator.comparing(ChannelDto::getTitle))
                .toList();
    }
}
