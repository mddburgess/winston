package ca.metricalsky.winston.service;

import ca.metricalsky.winston.dto.VideoDto;
import ca.metricalsky.winston.entity.Video;
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
    private final CommentService commentService;

    public Map<String, Long> countAllByChannelId() {
        var counts = videoRepository.countAllByChannelId()
                .stream()
                .collect(Collectors.toMap(VideoCount::getChannelId, VideoCount::getVideos));
        return defaultedMap(counts, 0L);
    }

    public List<VideoDto> findAllByAuthorHandle(String authorHandle) {
        var videos = videoRepository.findAllByCommentAuthorDisplayName(authorHandle);
        return populateCommentCounts(videos);
    }

    public List<VideoDto> findAllByChannelHandle(String channelHandle) {
        var videos = videoRepository.findAllByChannelHandle(channelHandle);
        return populateCommentCounts(videos);
    }

    public VideoDto getById(String videoId) {
        var videoDto = videoRepository.findById(videoId)
                .map(videoDtoMapper::fromEntity)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "The requested video was not found."));

        var commentCount = commentService.getCommentCountByVideoId(videoId);
        videoDto.setCommentCount(commentCount.getComments());
        videoDto.setReplyCount(commentCount.getReplies());
        videoDto.setTotalReplyCount(commentCount.getTotalReplies());

        return videoDto;
    }

    @Deprecated(since = "1.3.0", forRemoval = true)
    public List<VideoDto> getAllById(Iterable<String> videoIds) {
        var videos = videoRepository.findAllById(videoIds);
        return populateCommentCounts(videos);
    }

    private List<VideoDto> populateCommentCounts(List<Video> videos) {
        var videoIds = videos.stream()
                .map(Video::getId)
                .toList();
        var commentCounts = commentService.getCommentCountsByVideoIds(videoIds);
        return videos.stream()
                .map(videoDtoMapper::fromEntity)
                .peek(video -> video.setCommentCount(commentCounts.get(video.getId()).getComments()))
                .peek(video -> video.setReplyCount(commentCounts.get(video.getId()).getReplies()))
                .peek(video -> video.setTotalReplyCount(commentCounts.get(video.getId()).getTotalReplies()))
                .toList();
    }
}
