package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.dto.fetch.FetchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class AsyncFetchService {

    private final FetchRequestHandlerFactory fetchRequestHandlerFactory;

    @Async
    public void fetch(FetchRequest fetchRequest, SseEmitter sseEmitter) {
        try {
            var requestHandler = fetchRequestHandlerFactory.getHandlerForRequest(fetchRequest);
            requestHandler.fetch(fetchRequest, sseEmitter);
            sseEmitter.complete();
        } catch (Exception ex) {
            sseEmitter.completeWithError(ex);
        }
    }
}
