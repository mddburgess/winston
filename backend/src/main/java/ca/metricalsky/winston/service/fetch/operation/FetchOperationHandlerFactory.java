package ca.metricalsky.winston.service.fetch.operation;

import ca.metricalsky.winston.entity.fetch.FetchOperationEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FetchOperationHandlerFactory {

    private final FetchChannelsOperationHandler fetchChannelsOperationHandler;
    private final FetchCommentRepliesOperationHandler fetchCommentRepliesOperationHandler;
    private final FetchCommentsOperationHandler fetchCommentsOperationHandler;
    private final FetchVideoRepliesOperationHandler fetchVideoRepliesOperationHandler;
    private final FetchVideosOperationHandler fetchVideosOperationHandler;

    public FetchOperationHandler getHandler(FetchOperationEntity operation) {
        return switch (operation.getOperationType()) {
            case CHANNELS -> fetchChannelsOperationHandler;
            case VIDEOS -> fetchVideosOperationHandler;
            case COMMENTS -> fetchCommentsOperationHandler;
            case REPLIES -> switch (operation.getMode()) {
                case "FOR_COMMENT" -> fetchCommentRepliesOperationHandler;
                case "FOR_VIDEO" -> fetchVideoRepliesOperationHandler;
                default -> throw new IllegalArgumentException("Unsupported mode: " + operation.getMode());
            };
        };
    }
}
