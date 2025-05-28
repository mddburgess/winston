package ca.metricalsky.winston.dao;

import ca.metricalsky.winston.api.model.Video;
import ca.metricalsky.winston.mapper.dto.VideoMapper;
import ca.metricalsky.winston.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VideoDataService {

    private final VideoMapper videoMapper;
    private final VideoRepository videoRepository;

    public List<Video> getVideosForChannel(String channelHandle) {
        return videoRepository.findAllByChannelHandle(channelHandle)
                .stream()
                .map(videoMapper::toVideo)
                .toList();
    }

    public Optional<Video> findVideoById(String id) {
        return videoRepository.findChannelVideoById(id)
                .map(videoMapper::toVideo);
    }
}
