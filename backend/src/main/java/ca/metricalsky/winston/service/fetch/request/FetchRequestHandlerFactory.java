package ca.metricalsky.winston.service.fetch.request;

import ca.metricalsky.winston.entity.fetch.FetchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FetchRequestHandlerFactory {

    private final FetchChannelsRequestHandler fetchChannelsRequestHandler;
    private final FetchCommentRepliesRequestHandler fetchCommentRepliesRequestHandler;
    private final FetchCommentsRequestHandler fetchCommentsRequestHandler;
    private final FetchVideoRepliesRequestHandler fetchVideoRepliesRequestHandler;
    private final FetchVideosRequestHandler fetchVideosRequestHandler;

    public FetchRequestHandler getHandler(FetchRequest fetchRequest) {
        return switch (fetchRequest.getFetchType()) {
            case CHANNELS -> fetchChannelsRequestHandler;
            case VIDEOS -> fetchVideosRequestHandler;
            case COMMENTS -> fetchCommentsRequestHandler;
            case REPLIES -> switch (fetchRequest.getMode()) {
                case "FOR_COMMENT" -> fetchCommentRepliesRequestHandler;
                case "FOR_VIDEO" -> fetchVideoRepliesRequestHandler;
                default -> throw new IllegalArgumentException("Unsupported mode: " + fetchRequest.getMode());
            };
        };
    }
}
