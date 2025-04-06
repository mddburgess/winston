package ca.metricalsky.winston.service.fetch.request;

import ca.metricalsky.winston.entity.fetch.FetchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FetchRequestHandlerFactory {

    private final DefaultFetchRequestHandler defaultFetchRequestHandler;

    public FetchRequestHandler getHandler(FetchRequest fetchRequest) {
        return defaultFetchRequestHandler;
    }
}
