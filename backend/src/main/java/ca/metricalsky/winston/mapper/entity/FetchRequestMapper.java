package ca.metricalsky.winston.mapper.entity;

import ca.metricalsky.winston.api.model.FetchRequest;
import ca.metricalsky.winston.entity.fetch.FetchOperationEntity;
import ca.metricalsky.winston.entity.fetch.FetchOperationEntity.Type;
import ca.metricalsky.winston.entity.fetch.FetchRequestEntity;
import ca.metricalsky.winston.repository.VideoRepository;
import ca.metricalsky.winston.service.ChannelService;
import lombok.RequiredArgsConstructor;
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
        return switch (fetchRequest.getFetch()) {
            case CHANNEL -> channelRequest(fetchRequest);
            case VIDEOS -> videosRequest(fetchRequest);
            case COMMENTS -> commentsRequest(fetchRequest);
            case REPLIES -> repliesRequest(fetchRequest);
            default -> throw new IllegalStateException("Unexpected value: " + fetchRequest.getFetch());
        };
    }

    private FetchOperationEntity channelRequest(FetchRequest fetchRequest) {
        return FetchOperationEntity.builder()
                .operationType(Type.CHANNELS)
                .objectId(fetchRequest.getChannelHandle())
                .build();
    }

    private FetchOperationEntity videosRequest(FetchRequest fetchRequest) {
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

    private FetchOperationEntity commentsRequest(FetchRequest fetchRequest) {
        return FetchOperationEntity.builder()
                .operationType(Type.COMMENTS)
                .objectId(fetchRequest.getVideoId())
                .build();
    }

    private FetchOperationEntity repliesRequest(FetchRequest fetchRequest) {
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
