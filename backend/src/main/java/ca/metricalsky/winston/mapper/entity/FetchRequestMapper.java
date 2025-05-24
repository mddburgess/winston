package ca.metricalsky.winston.mapper.entity;

import ca.metricalsky.winston.dto.fetch.FetchChannel;
import ca.metricalsky.winston.dto.fetch.FetchComments;
import ca.metricalsky.winston.dto.fetch.FetchReplies;
import ca.metricalsky.winston.dto.fetch.FetchRequest;
import ca.metricalsky.winston.dto.fetch.FetchVideos;
import ca.metricalsky.winston.entity.fetch.FetchOperationEntity;
import ca.metricalsky.winston.entity.fetch.FetchOperationEntity.Type;
import ca.metricalsky.winston.entity.fetch.FetchRequestEntity;
import ca.metricalsky.winston.exception.AppException;
import ca.metricalsky.winston.repository.VideoRepository;
import ca.metricalsky.winston.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
@RequiredArgsConstructor
public class FetchRequestMapper {

    private final ChannelService channelService;
    private final VideoRepository videoRepository;

    public FetchRequestEntity toFetchRequest(FetchRequest fetchRequest) {
        return FetchRequestEntity.builder()
                .operations(List.of(toFetchOperation(fetchRequest)))
                .build();
    }

    private FetchOperationEntity toFetchOperation(FetchRequest fetchRequest) {
        if (fetchRequest.getChannel() != null) {
            return channelRequest(fetchRequest.getChannel());
        }
        if (fetchRequest.getVideos() != null) {
            return videosRequest(fetchRequest.getVideos());
        }
        if (fetchRequest.getComments() != null) {
            return commentsRequest(fetchRequest.getComments());
        }
        if (fetchRequest.getReplies() != null) {
            return repliesRequest(fetchRequest.getReplies());
        }
        throw new AppException(HttpStatus.BAD_REQUEST, "The request is syntactically invalid and cannot be processed.");
    }

    private FetchOperationEntity channelRequest(FetchChannel fetchChannel) {
        return FetchOperationEntity.builder()
                .operationType(Type.CHANNELS)
                .objectId(fetchChannel.getHandle())
                .build();
    }

    private FetchOperationEntity videosRequest(FetchVideos fetchVideos) {
        channelService.requireChannelExists(fetchVideos.getChannelId());

        var builder = FetchOperationEntity.builder()
                .operationType(Type.VIDEOS)
                .objectId(fetchVideos.getChannelId())
                .mode(fetchVideos.getFetch().toString());

        if (fetchVideos.getFetch() == FetchVideos.Mode.LATEST) {
            var publishedAfter = videoRepository.findLastPublishedAtForChannelId(fetchVideos.getChannelId())
                    .map(date -> date.plusSeconds(1))
                    .orElse(null);
            builder.publishedAfter(publishedAfter);
        } else if (fetchVideos.getRange() != null) {
            builder.publishedAfter(fetchVideos.getRange().getAfter())
                    .publishedBefore(fetchVideos.getRange().getBefore());
        }

        return builder.build();
    }

    private FetchOperationEntity commentsRequest(FetchComments fetchComments) {
        return FetchOperationEntity.builder()
                .operationType(Type.COMMENTS)
                .objectId(fetchComments.getVideoId())
                .build();
    }

    private FetchOperationEntity repliesRequest(FetchReplies fetchReplies) {
        var builder = FetchOperationEntity.builder()
                .operationType(Type.REPLIES);

        if (isNotBlank(fetchReplies.getCommentId())) {
            builder.objectId(fetchReplies.getCommentId())
                    .mode("FOR_COMMENT");
        } else if (isNotBlank(fetchReplies.getVideoId())) {
            builder.objectId(fetchReplies.getVideoId())
                    .mode("FOR_VIDEO");
        }

        return builder.build();
    }
}
