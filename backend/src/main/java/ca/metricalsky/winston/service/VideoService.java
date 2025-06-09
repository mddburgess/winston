package ca.metricalsky.winston.service;

import ca.metricalsky.winston.dto.VideoDto;
import ca.metricalsky.winston.entity.view.VideoCountView;
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
}
