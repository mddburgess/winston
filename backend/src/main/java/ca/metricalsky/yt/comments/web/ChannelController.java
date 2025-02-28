package ca.metricalsky.yt.comments.web;

import ca.metricalsky.yt.comments.dto.ChannelDto;
import ca.metricalsky.yt.comments.mapper.ChannelMapper;
import ca.metricalsky.yt.comments.repository.ChannelRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/channels")
public class ChannelController {

    private final ChannelMapper channelMapper = Mappers.getMapper(ChannelMapper.class);

    private final ChannelRepository channelRepository;

    @Autowired
    public ChannelController(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @GetMapping
    public List<ChannelDto> list() {
        return channelRepository.findAll()
                .stream()
                .map(channelMapper::toDto)
                .sorted(Comparator.comparing(ChannelDto::getTitle))
                .toList();
    }
}
