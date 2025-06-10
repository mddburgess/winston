package ca.metricalsky.winston.mapper.entity;

import ca.metricalsky.winston.api.model.FetchRequest;
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

    public FetchRequestEntity toFetchRequestEntity(FetchRequest fetchRequest) {
        return FetchRequestEntity.builder()
                .operations(List.of(toFetchOperationEntity(fetchRequest)))
                .build();
    }

    private FetchOperationEntity toFetchOperationEntity(FetchRequest fetchRequest) {
        if (fetchRequest.getFetch() == null) {
            throw new AppException(HttpStatus.BAD_REQUEST, "The request is syntactically invalid and cannot be processed.");
        }
        return switch (fetchRequest.getFetch()) {
            case CHANNEL -> buildFetchChannelOperation(fetchRequest);
            case VIDEOS -> buildFetchVideosOperation(fetchRequest);
            case COMMENTS -> buildFetchCommentsOperation(fetchRequest);
            case REPLIES -> buildFetchRepliesOperation(fetchRequest);
        };
    }

    private FetchOperationEntity buildFetchChannelOperation(FetchRequest fetchRequest) {
        return FetchOperationEntity.builder()
                .operationType(Type.CHANNELS)
                .objectId(fetchRequest.getChannelHandle())
                .build();
    }

    private FetchOperationEntity buildFetchVideosOperation(FetchRequest fetchRequest) {
        channelService.requireChannelExists(fetchRequest.getChannelId());

        var builder = FetchOperationEntity.builder()
                .operationType(Type.VIDEOS)
                .objectId(fetchRequest.getChannelId())
                .mode(fetchRequest.getRange() != null ? fetchRequest.getRange().name() : null);

        if (fetchRequest.getRange() == FetchRequest.RangeEnum.LATEST) {
            var publishedAfter = videoRepository.findLastPublishedAtForChannelId(fetchRequest.getChannelId())
                    .map(date -> date.plusSeconds(1))
                    .orElse(null);
            builder.publishedAfter(publishedAfter);
        }

        return builder.build();
    }

    private FetchOperationEntity buildFetchCommentsOperation(FetchRequest fetchRequest) {
        return FetchOperationEntity.builder()
                .operationType(Type.COMMENTS)
                .objectId(fetchRequest.getVideoId())
                .build();
    }

    private FetchOperationEntity buildFetchRepliesOperation(FetchRequest fetchRequest) {
        var builder = FetchOperationEntity.builder()
                .operationType(Type.REPLIES);

        if (isNotBlank(fetchRequest.getCommentId())) {
            builder.objectId(fetchRequest.getCommentId())
                    .mode("FOR_COMMENT");
        } else if (isNotBlank(fetchRequest.getVideoId())) {
            builder.objectId(fetchRequest.getVideoId())
                    .mode("FOR_VIDEO");
        }

        return builder.build();
    }
}
