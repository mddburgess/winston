package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.entity.fetch.FetchAction;
import ca.metricalsky.winston.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FetchActionHandlerFactory {

    private final FetchChannelActionHandler fetchChannelActionHandler;
    private final FetchVideosActionHandler fetchVideosActionHandler;
    private final FetchCommentsActionHandler fetchCommentsActionHandler;

    public FetchActionHandler<?> getHandlerForAction(FetchAction fetchAction) {
        return switch (fetchAction.getActionType()) {
            case CHANNELS -> fetchChannelActionHandler;
            case VIDEOS -> fetchVideosActionHandler;
            case COMMENTS -> fetchCommentsActionHandler;
            default -> throw new AppException(HttpStatus.NOT_IMPLEMENTED,
                    "The requested fetch action is not supported.");
        };
    }
}
