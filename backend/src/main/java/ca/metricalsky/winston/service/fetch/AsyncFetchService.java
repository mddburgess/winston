package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.dto.fetch.FetchRequestDto;
import ca.metricalsky.winston.mapper.entity.FetchRequestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class AsyncFetchService {

    private final FetchRequestMapper fetchRequestMapper;
    private final FetchRequestHandlerFactory fetchRequestHandlerFactory;

    @Async
    public void fetch(FetchRequestDto fetchRequestDto, SseEmitter sseEmitter) {
        try {
            var fetchRequest = fetchRequestMapper.toFetchRequest(fetchRequestDto);
            var requestHandler = fetchRequestHandlerFactory.getHandlerForRequest(fetchRequest);
            requestHandler.fetch(fetchRequest, sseEmitter);
            sseEmitter.complete();
        } catch (Exception ex) {
            sseEmitter.completeWithError(ex);
        }
    }
}
