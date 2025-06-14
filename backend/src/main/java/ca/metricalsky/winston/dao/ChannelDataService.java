package ca.metricalsky.winston.dao;

import ca.metricalsky.winston.api.model.Channel;
import ca.metricalsky.winston.mapper.entity.ChannelEntityMapper;
import ca.metricalsky.winston.mappers.api.ChannelMapper;
import ca.metricalsky.winston.repository.ChannelRepository;
import com.google.api.services.youtube.model.ChannelListResponse;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChannelDataService {

    private final ChannelEntityMapper channelEntityMapper = Mappers.getMapper(ChannelEntityMapper.class);

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

    public Optional<Channel> saveChannel(ChannelListResponse channelListResponse) {
        return Optional.ofNullable(channelListResponse)
                .map(ChannelListResponse::getItems)
                .orElse(Collections.emptyList())
                .stream()
                .findFirst()
                .map(channelEntityMapper::toChannelEntity)
                .map(channelRepository::save)
                .map(channelMapper::toChannel);
    }
}
