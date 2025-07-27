package ca.metricalsky.winston.mapper.entity;

import ca.metricalsky.winston.api.model.PullChannelOperation;
import ca.metricalsky.winston.api.model.PullCommentsOperation;
import ca.metricalsky.winston.api.model.PullOperation;
import ca.metricalsky.winston.api.model.PullRepliesOperation;
import ca.metricalsky.winston.api.model.PullRequest;
import ca.metricalsky.winston.api.model.PullVideosOperation;
import ca.metricalsky.winston.entity.fetch.FetchOperationEntity;
import ca.metricalsky.winston.entity.fetch.FetchRequestEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Mapper(componentModel = "spring")
public abstract class FetchRequestEntityMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "lastUpdatedAt", ignore = true)
    public abstract FetchRequestEntity toFetchRequestEntity(PullRequest pullRequest);

    protected FetchOperationEntity toFetchOperationEntity(PullOperation pullOperation) {
        return switch (pullOperation) {
            case PullChannelOperation pullChannelOperation -> toFetchOperationEntity(pullChannelOperation);
            case PullVideosOperation pullVideosOperation -> toFetchOperationEntity(pullVideosOperation);
            case PullCommentsOperation pullCommentsOperation -> toFetchOperationEntity(pullCommentsOperation);
            case PullRepliesOperation pullRepliesOperation -> toFetchOperationEntity(pullRepliesOperation);
            default -> null;
        };
    }

    @Mapping(target = "operationType", constant = "CHANNELS")
    @Mapping(target = "objectId", source = "channelHandle")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fetchRequestId", ignore = true)
    @Mapping(target = "mode", ignore = true)
    @Mapping(target = "publishedAfter", ignore = true)
    @Mapping(target = "publishedBefore", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "error", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "lastUpdatedAt", ignore = true)
    protected abstract FetchOperationEntity toFetchOperationEntity(PullChannelOperation pullChannelOperation);

    @Mapping(target = "operationType", constant = "VIDEOS")
    @Mapping(target = "objectId", source = "channelHandle")
    @Mapping(target = "mode", source = "range")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fetchRequestId", ignore = true)
    @Mapping(target = "publishedAfter", ignore = true)
    @Mapping(target = "publishedBefore", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "error", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "lastUpdatedAt", ignore = true)
    protected abstract FetchOperationEntity toFetchOperationEntity(PullVideosOperation pullVideosOperation);

    @Mapping(target = "operationType", constant = "COMMENTS")
    @Mapping(target = "objectId", source = "videoId")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fetchRequestId", ignore = true)
    @Mapping(target = "mode", ignore = true)
    @Mapping(target = "publishedAfter", ignore = true)
    @Mapping(target = "publishedBefore", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "error", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "lastUpdatedAt", ignore = true)
    protected abstract FetchOperationEntity toFetchOperationEntity(PullCommentsOperation pullCommentsOperation);

    @Mapping(target = "operationType", constant = "REPLIES")
    @Mapping(target = "objectId", source = ".", qualifiedByName = "getPullRepliesObjectId")
    @Mapping(target = "mode", source = ".", qualifiedByName = "getPullRepliesMode")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fetchRequestId", ignore = true)
    @Mapping(target = "publishedAfter", ignore = true)
    @Mapping(target = "publishedBefore", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "error", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "lastUpdatedAt", ignore = true)
    protected abstract FetchOperationEntity toFetchOperationEntity(PullRepliesOperation pullRepliesOperation);

    @Named("getPullRepliesObjectId")
    protected String getPullRepliesObjectId(PullRepliesOperation pullRepliesOperation) {
        if (isNotBlank(pullRepliesOperation.getCommentId())) {
            return pullRepliesOperation.getCommentId();
        }
        return pullRepliesOperation.getVideoId();
    }

    @Named("getPullRepliesMode")
    protected String getPullRepliesMode(PullRepliesOperation pullRepliesOperation) {
        if (isNotBlank(pullRepliesOperation.getCommentId())) {
            return "FOR_COMMENT";
        }
        return "FOR_VIDEO";
    }
}
