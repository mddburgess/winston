package ca.metricalsky.winston.dao;

import ca.metricalsky.winston.api.model.Channel;
import ca.metricalsky.winston.database.entity.channel.ChannelPropertiesEntity;
import ca.metricalsky.winston.database.repository.channel.ChannelPropertiesRepository;
import ca.metricalsky.winston.entity.ChannelEntity;
import ca.metricalsky.winston.mapper.entity.ChannelEntityMapper;
import ca.metricalsky.winston.repository.ChannelRepository;
import com.google.api.services.youtube.model.ChannelListResponse;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.core.convert.ConversionService;
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

    private final ChannelPropertiesRepository channelPropertiesRepository;
    private final ChannelRepository channelRepository;
    private final ConversionService conversionService;

    public List<Channel> getAllChannels(boolean includeArchived) {
        var channels = includeArchived
                ? channelRepository.findAll()
                : channelRepository.findAllUnarchived();

        return channels.stream()
                .map(this::convert)
                .toList();
    }

    public Optional<String> findChannelIdByHandle(String handle) {
        return channelRepository.findIdByCustomUrl(handle);
    }

    public Optional<Channel> findChannelByHandle(String handle) {
        return channelRepository.findByCustomUrl(handle)
                .map(this::convert);
    }

    public Optional<Channel> saveChannel(ChannelListResponse channelListResponse) {
        return Optional.ofNullable(channelListResponse)
                .map(ChannelListResponse::getItems)
                .orElse(Collections.emptyList())
                .stream()
                .findFirst()
                .map(channelEntityMapper::toChannelEntity)
                .map(channelRepository::save)
                .map(this::convert);
    }

    public void saveChannelProperties(Channel channel) {
        var channelProperties = new ChannelPropertiesEntity(channel.getId(), channel.getProperties().getArchived());
        channelPropertiesRepository.save(channelProperties);
    }

    private Channel convert(ChannelEntity channelEntity) {
        return conversionService.convert(channelEntity, Channel.class);
    }
}
