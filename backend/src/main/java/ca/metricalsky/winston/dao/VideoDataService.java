package ca.metricalsky.winston.dao;

import ca.metricalsky.winston.api.model.Video;
import ca.metricalsky.winston.entity.view.VideoCountView;
import ca.metricalsky.winston.mapper.entity.VideoEntityMapper;
import ca.metricalsky.winston.mappers.api.VideoMapper;
import ca.metricalsky.winston.repository.VideoRepository;
import com.google.api.services.youtube.model.ActivityListResponse;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.map.DefaultedMap.defaultedMap;

@Service
@Transactional
@RequiredArgsConstructor
public class VideoDataService {

    private final VideoEntityMapper videoEntityMapper = Mappers.getMapper(VideoEntityMapper.class);

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

    public List<Video> saveVideos(ActivityListResponse activityListResponse) {
        var videoEntities = Optional.ofNullable(activityListResponse)
                .map(ActivityListResponse::getItems)
                .orElse(Collections.emptyList())
                .stream()
                .filter(activity -> activity.getContentDetails().getUpload() != null)
                .map(videoEntityMapper::toVideoEntity)
                .toList();

        videoEntities = videoRepository.saveAll(videoEntities);

        return videoEntities.stream()
                .map(videoMapper::toVideo)
                .toList();
    }
}
