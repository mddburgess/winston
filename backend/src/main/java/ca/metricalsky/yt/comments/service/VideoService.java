package ca.metricalsky.yt.comments.service;

import ca.metricalsky.yt.comments.dto.VideoDto;
import ca.metricalsky.yt.comments.mapper.VideoMapper;
import ca.metricalsky.yt.comments.repository.VideoRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.map.DefaultedMap.defaultedMap;

@Service
public class VideoService {

    private final VideoMapper videoMapper = Mappers.getMapper(VideoMapper.class);
    private final VideoRepository videoRepository;
    private final CommentService commentService;

    @Autowired
    public VideoService(VideoRepository videoRepository, CommentService commentService) {
        this.videoRepository = videoRepository;
        this.commentService = commentService;
    }

    public Map<String, Long> countAllByChannelId() {
        var counts = videoRepository.countAllByChannelId()
                .stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(0, String.class),
                        tuple -> tuple.get(1, Long.class)
                ));
        return defaultedMap(counts, 0L);
    }

    @Transactional(readOnly = true)
    public List<VideoDto> findAllByChannelId(String channelId) {
        var commentCounts = commentService.getCommentCountsByChannelId(channelId);
        var replyCounts = commentService.getReplyCountsByChannelId(channelId);
        return videoRepository.findAllByChannelId(channelId)
                .stream()
                .map(videoMapper::toDto)
                .peek(video -> video.setCommentCount(commentCounts.get(video.getId()).comments()))
                .peek(video -> video.setReplyCount(replyCounts.get(video.getId()).comments()))
                .peek(video -> video.setTotalReplyCount(commentCounts.get(video.getId()).replies()))
                .toList();
    }

    public VideoDto getById(String videoId) {
        return videoRepository.findById(videoId)
                .map(videoMapper::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
