package ca.metricalsky.winston.service.fetch.request;

import ca.metricalsky.winston.entity.fetch.FetchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FetchRequestHandlerFactory {

    private final DefaultFetchRequestHandler defaultFetchRequestHandler;
    private final FetchVideoRepliesRequestHandler fetchVideoRepliesRequestHandler;

    public FetchRequestHandler getHandler(FetchRequest fetchRequest) {
        return switch (fetchRequest.getFetchType()) {
            case CHANNELS, VIDEOS, COMMENTS -> defaultFetchRequestHandler;
            case REPLIES -> {
                if ("FOR_VIDEO".equals(fetchRequest.getMode())) {
                    yield fetchVideoRepliesRequestHandler;
                }
                yield defaultFetchRequestHandler;
            }
        };
    }
}
