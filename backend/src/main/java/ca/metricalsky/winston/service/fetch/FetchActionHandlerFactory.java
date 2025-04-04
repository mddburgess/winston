package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.entity.fetch.FetchAction;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
            default -> throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
        };
    }
}
