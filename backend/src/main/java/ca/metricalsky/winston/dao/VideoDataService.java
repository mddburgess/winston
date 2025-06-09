package ca.metricalsky.winston.dao;

import ca.metricalsky.winston.api.model.Video;
import ca.metricalsky.winston.entity.view.VideoCountView;
import ca.metricalsky.winston.mappers.api.VideoMapper;
import ca.metricalsky.winston.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.map.DefaultedMap.defaultedMap;

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

    public List<Video> getVideosForAuthor(String authorHandle) {
        return videoRepository.findAllChannelVideosByAuthorDisplayName(authorHandle)
                .stream()
                .map(videoMapper::toVideo)
                .toList();
    }

    public Map<String, Integer> countAllVideosByChannelId() {
        var counts = videoRepository.countAllByChannelId()
                .stream()
                .collect(Collectors.toMap(VideoCountView::getChannelId, VideoCountView::getVideos));
        return defaultedMap(counts, 0);
    }
}
