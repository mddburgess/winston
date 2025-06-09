package ca.metricalsky.winston.service;

import ca.metricalsky.winston.api.model.Channel;
import ca.metricalsky.winston.dao.ChannelDataService;
import ca.metricalsky.winston.dao.VideoDataService;
import ca.metricalsky.winston.exception.AppException;
import ca.metricalsky.winston.repository.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChannelService {

    private final ChannelDataService channelDataService;
    private final ChannelRepository channelRepository;
    private final VideoDataService videoDataService;

    public List<Channel> getAllChannels() {
        var channels = channelDataService.getAllChannels();
        var videoCounts = videoDataService.countAllVideosByChannelId();

        return channels.stream()
                .map(channel -> channel.videoCount(videoCounts.get(channel.getId())))
                .sorted(Comparator.comparing(Channel::getTitle))
                .toList();
    }

    public void requireChannelExists(String channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new AppException(HttpStatus.NOT_FOUND, "The requested channel was not found.");
        };
    }
}
