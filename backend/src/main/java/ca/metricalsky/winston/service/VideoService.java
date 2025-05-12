package ca.metricalsky.winston.service;

import ca.metricalsky.winston.dto.VideoDto;
import ca.metricalsky.winston.entity.view.VideoCount;
import ca.metricalsky.winston.exception.AppException;
import ca.metricalsky.winston.mapper.dto.VideoDtoMapper;
import ca.metricalsky.winston.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.map.DefaultedMap.defaultedMap;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoDtoMapper videoDtoMapper = Mappers.getMapper(VideoDtoMapper.class);
    private final VideoRepository videoRepository;

    public Map<String, Long> countAllByChannelId() {
        var counts = videoRepository.countAllByChannelId()
                .stream()
                .collect(Collectors.toMap(VideoCount::getChannelId, VideoCount::getVideos));
        return defaultedMap(counts, 0L);
    }

    public List<VideoDto> findAllByAuthorHandle(String authorHandle) {
        return videoRepository.findAllByCommentAuthorDisplayName(authorHandle)
                .stream()
                .map(videoDtoMapper::fromEntity)
                .toList();
    }

    public List<VideoDto> findAllByChannelHandle(String channelHandle) {
        return videoRepository.findAllByChannelHandle(channelHandle)
                .stream()
                .map(videoDtoMapper::fromEntity)
                .toList();
    }

    public VideoDto getById(String videoId) {
        return videoRepository.findById(videoId)
                .map(videoDtoMapper::fromEntity)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "The requested video was not found."));
    }

    @Deprecated(since = "1.3.0", forRemoval = true)
    public List<VideoDto> getAllById(Iterable<String> videoIds) {
        return videoRepository.findAllById(videoIds)
                .stream()
                .map(videoDtoMapper::fromEntity)
                .toList();
    }
}
