package ca.metricalsky.winston.service;

import ca.metricalsky.winston.dto.VideoDto;
import ca.metricalsky.winston.entity.view.VideoCount;
import ca.metricalsky.winston.mapper.dto.VideoDtoMapper;
import ca.metricalsky.winston.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.map.DefaultedMap.defaultedMap;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoDtoMapper videoDtoMapper = Mappers.getMapper(VideoDtoMapper.class);
    private final VideoRepository videoRepository;
    private final CommentService commentService;

    public Map<String, Long> countAllByChannelId() {
        var counts = videoRepository.countAllByChannelId()
                .stream()
                .collect(Collectors.toMap(VideoCount::getChannelId, VideoCount::getVideos));
        return defaultedMap(counts, 0L);
    }

    @Transactional(readOnly = true)
    public List<VideoDto> findAllByChannelId(String channelId) {
        var commentCounts = commentService.getCommentCountsByChannelId(channelId);
        return videoRepository.findAllByChannelIdOrderByPublishedAtDesc(channelId)
                .stream()
                .map(videoDtoMapper::fromEntity)
                .peek(video -> video.setCommentCount(commentCounts.get(video.getId()).getComments()))
                .peek(video -> video.setReplyCount(commentCounts.get(video.getId()).getReplies()))
                .peek(video -> video.setTotalReplyCount(commentCounts.get(video.getId()).getTotalReplies()))
                .toList();
    }

    public VideoDto getById(String videoId) {
        var videoDto = videoRepository.findById(videoId)
                .map(videoDtoMapper::fromEntity)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var commentCount = commentService.getCommentCountByVideoId(videoId);
        videoDto.setCommentCount(commentCount.getComments());
        videoDto.setReplyCount(commentCount.getReplies());
        videoDto.setTotalReplyCount(commentCount.getTotalReplies());

        return videoDto;
    }

    public List<VideoDto> getAllById(Iterable<String> videoIds) {
        var commentCounts = commentService.getCommentCountsByVideoIds(videoIds);
        return videoRepository.findAllById(videoIds)
                .stream()
                .map(videoDtoMapper::fromEntity)
                .peek(video -> video.setCommentCount(commentCounts.get(video.getId()).getComments()))
                .peek(video -> video.setReplyCount(commentCounts.get(video.getId()).getReplies()))
                .peek(video -> video.setTotalReplyCount(commentCounts.get(video.getId()).getTotalReplies()))
                .toList();
    }
}
