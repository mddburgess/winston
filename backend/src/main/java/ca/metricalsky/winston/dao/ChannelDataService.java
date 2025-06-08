package ca.metricalsky.winston.dao;

import ca.metricalsky.winston.api.model.Channel;
import ca.metricalsky.winston.mappers.api.ChannelMapper;
import ca.metricalsky.winston.repository.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChannelDataService {

    private final ChannelMapper channelMapper;
    private final ChannelRepository channelRepository;

    public List<Channel> getAllChannels() {
        return channelRepository.findAll()
                .stream()
                .map(channelMapper::toChannel)
                .toList();
    }

    public Optional<Channel> findChannelByHandle(String handle) {
        return channelRepository.findByCustomUrl(handle)
                .map(channelMapper::toChannel);
    }
}
