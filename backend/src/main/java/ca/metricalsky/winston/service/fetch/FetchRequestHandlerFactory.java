package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.entity.fetch.FetchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class FetchRequestHandlerFactory {

    private final FetchChannelService fetchChannelService;
    private final FetchVideosService fetchVideosService;
    private final FetchCommentsService fetchCommentsService;

    public FetchRequestHandler getHandlerForRequest(FetchRequest fetchRequest) {
        return switch (fetchRequest.getFetchType()) {
            case CHANNEL -> fetchChannelService;
            case VIDEOS -> fetchVideosService;
            case COMMENTS -> fetchCommentsService;
            default -> throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
        };
    }
}
