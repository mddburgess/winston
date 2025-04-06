package ca.metricalsky.winston.mapper.entity;

import ca.metricalsky.winston.dto.fetch.FetchChannel;
import ca.metricalsky.winston.dto.fetch.FetchComments;
import ca.metricalsky.winston.dto.fetch.FetchReplies;
import ca.metricalsky.winston.dto.fetch.FetchRequestDto;
import ca.metricalsky.winston.dto.fetch.FetchVideos;
import ca.metricalsky.winston.entity.fetch.FetchRequest;
import ca.metricalsky.winston.entity.fetch.FetchRequest.FetchType;
import ca.metricalsky.winston.exception.AppException;
import ca.metricalsky.winston.repository.VideoRepository;
import ca.metricalsky.winston.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FetchRequestMapper {

    private final ChannelService channelService;
    private final VideoRepository videoRepository;

    public FetchRequest toFetchRequest(FetchRequestDto fetchRequestDto) {
        if (fetchRequestDto.getChannel() != null) {
            return channelRequest(fetchRequestDto.getChannel());
        }
        if (fetchRequestDto.getVideos() != null) {
            return videosRequest(fetchRequestDto.getVideos());
        }
        if (fetchRequestDto.getComments() != null) {
            return commentsRequest(fetchRequestDto.getComments());
        }
        if (fetchRequestDto.getReplies() != null) {
            return repliesRequest(fetchRequestDto.getReplies());
        }
        throw new AppException(HttpStatus.BAD_REQUEST, "The request is syntactically invalid and cannot be processed.");
    }

    private FetchRequest channelRequest(FetchChannel fetchChannel) {
        var fetchRequest = new FetchRequest();
        fetchRequest.setFetchType(FetchType.CHANNELS);
        fetchRequest.setObjectId(fetchChannel.getHandle());
        return fetchRequest;
    }

    private FetchRequest videosRequest(FetchVideos fetchVideos) {
        channelService.requireChannelExists(fetchVideos.getChannelId());

        var fetchRequest = new FetchRequest();
        fetchRequest.setFetchType(FetchType.VIDEOS);
        fetchRequest.setObjectId(fetchVideos.getChannelId());
        fetchRequest.setMode(fetchVideos.getFetch().toString());
        if (fetchVideos.getFetch() == FetchVideos.Mode.LATEST) {
            var publishedAfter = videoRepository.findLastPublishedAtForChannelId(fetchVideos.getChannelId())
                    .map(date -> date.plusSeconds(1))
                    .orElse(null);
            fetchRequest.setPublishedAfter(publishedAfter);
        } else if (fetchVideos.getRange() != null) {
            fetchRequest.setPublishedAfter(fetchVideos.getRange().getAfter());
            fetchRequest.setPublishedBefore(fetchVideos.getRange().getBefore());
        }
        return fetchRequest;
    }

    private FetchRequest commentsRequest(FetchComments fetchComments) {
        var fetchRequest = new FetchRequest();
        fetchRequest.setFetchType(FetchType.COMMENTS);
        fetchRequest.setObjectId(fetchComments.getVideoId());
        return fetchRequest;
    }

    private FetchRequest repliesRequest(FetchReplies fetchReplies) {
        var fetchRequest = new FetchRequest();
        fetchRequest.setFetchType(FetchType.REPLIES);
        fetchRequest.setObjectId(fetchReplies.getCommentId());
        return fetchRequest;
    }
}
