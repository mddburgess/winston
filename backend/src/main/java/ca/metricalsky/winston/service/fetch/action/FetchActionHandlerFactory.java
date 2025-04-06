package ca.metricalsky.winston.service.fetch.action;

import ca.metricalsky.winston.entity.fetch.FetchAction;
import ca.metricalsky.winston.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FetchActionHandlerFactory {

    private final FetchChannelActionHandler fetchChannelActionHandler;
    private final FetchCommentsActionHandler fetchCommentsActionHandler;
    private final FetchVideosActionHandler fetchVideosActionHandler;
    private final FetchRepliesActionHandler fetchRepliesActionHandler;

    public FetchActionHandler<?> getHandlerForAction(FetchAction fetchAction) {
        if (fetchAction.getActionType() == null) {
            throw new AppException(HttpStatus.BAD_REQUEST, "The fetch action must have a valid action type.");
        }
        return switch (fetchAction.getActionType()) {
            case CHANNELS -> fetchChannelActionHandler;
            case VIDEOS -> fetchVideosActionHandler;
            case COMMENTS -> fetchCommentsActionHandler;
            case REPLIES -> fetchRepliesActionHandler;
        };
    }
}
