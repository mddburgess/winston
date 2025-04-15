package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.dto.fetch.FetchRequestDto;
import ca.metricalsky.winston.events.FetchStatusEvent;
import ca.metricalsky.winston.events.SsePublisher;
import ca.metricalsky.winston.mapper.entity.FetchRequestMapper;
import ca.metricalsky.winston.service.fetch.request.FetchRequestHandlerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AsyncFetchService {

    private final FetchRequestHandlerFactory fetchRequestHandlerFactory;
    private final FetchRequestMapper fetchRequestMapper;

    @Async
    public void fetch(FetchRequestDto fetchRequestDto, SsePublisher ssePublisher) {
        var fetchRequest = fetchRequestMapper.toFetchRequest(fetchRequestDto);
        try {
            fetchRequestHandlerFactory.getHandler(fetchRequest)
                    .fetch(fetchRequest, ssePublisher);
            ssePublisher.publish(FetchStatusEvent.completed());
            ssePublisher.complete();
        } catch (RuntimeException ex) {
            if (ssePublisher.isOpen()) {
                ssePublisher.publish(FetchStatusEvent.failed(ex));
                ssePublisher.completeWithError(ex);
            }
        }
    }
}
