package ca.metricalsky.winston.dao;

import ca.metricalsky.winston.api.model.Video;
import ca.metricalsky.winston.database.entity.video.VideoEntity;
import ca.metricalsky.winston.database.view.VideoCountView;
import ca.metricalsky.winston.mappers.api.VideoMapper;
import ca.metricalsky.winston.database.repository.video.VideoRepository;
import com.google.api.services.youtube.model.VideoListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    private final ConversionService conversionService;
    private final VideoMapper videoMapper;
    private final VideoRepository videoRepository;

    public List<Video> listVideosByChannelId(String channelId, PageRequest page) {
        var pageRequest = page.withSort(Sort.Direction.DESC, "publishedAt");

        return videoRepository.findPageByChannelId(channelId, pageRequest)
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

    public Integer countByChannelId(String channelId) {
        return videoRepository.countByChannelId(channelId);
    }

    public Map<String, Integer> countAllVideosByChannelId() {
        var counts = videoRepository.countAllGroupByChannelId()
                .stream()
                .collect(Collectors.toMap(VideoCountView::getChannelId, VideoCountView::getVideos));
        return defaultedMap(counts, 0);
    }

    public List<Video> saveVideos(VideoListResponse videoListResponse) {
        var videoEntities = Optional.ofNullable(videoListResponse)
                .map(VideoListResponse::getItems)
                .orElse(Collections.emptyList())
                .stream()
                .map(video -> conversionService.convert(video, VideoEntity.class))
                .toList();

        videoEntities = videoRepository.saveAll(videoEntities);

        return videoEntities.stream()
                .map(videoMapper::toVideo)
                .toList();
    }
}
