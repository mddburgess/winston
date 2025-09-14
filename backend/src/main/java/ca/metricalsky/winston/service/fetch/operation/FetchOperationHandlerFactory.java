package ca.metricalsky.winston.service.fetch.operation;

import ca.metricalsky.winston.api.model.Channel;
import ca.metricalsky.winston.api.model.Comment;
import ca.metricalsky.winston.api.model.TopLevelComment;
import ca.metricalsky.winston.api.model.Video;
import ca.metricalsky.winston.entity.fetch.FetchOperationEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FetchOperationHandlerFactory {

    private final FetchOperationHandler<Channel> fetchChannelsOperationHandler;
    private final FetchOperationHandler<Video> fetchVideosOperationHandler;
    private final FetchOperationHandler<TopLevelComment> fetchCommentsOperationHandler;
    private final FetchOperationHandler<Comment> fetchCommentRepliesOperationHandler;
    private final FetchOperationHandler<Comment> fetchVideoRepliesOperationHandler;

    public FetchOperationHandler getHandler(FetchOperationEntity fetchOperation) {
        return switch (fetchOperation.getOperationType()) {
            case CHANNELS -> fetchChannelsOperationHandler;
            case VIDEOS -> fetchVideosOperationHandler;
            case COMMENTS -> fetchCommentsOperationHandler;
            case REPLIES -> switch (fetchOperation.getMode()) {
                case "FOR_COMMENT" -> fetchCommentRepliesOperationHandler;
                case "FOR_VIDEO" -> fetchVideoRepliesOperationHandler;
                default -> throw new IllegalArgumentException("Unsupported mode: " + fetchOperation.getMode());
            };
        };
    }
}
