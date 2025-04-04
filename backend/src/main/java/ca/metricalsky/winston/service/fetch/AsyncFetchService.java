package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.dto.fetch.FetchRequestDto;
import ca.metricalsky.winston.entity.fetch.FetchAction;
import ca.metricalsky.winston.entity.fetch.FetchRequest;
import ca.metricalsky.winston.events.FetchEvent;
import ca.metricalsky.winston.events.FetchStatus;
import ca.metricalsky.winston.mapper.entity.FetchRequestMapper;
import ca.metricalsky.winston.utils.SseEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
@Slf4j
public class AsyncFetchService {

    private final FetchActionHandlerFactory fetchActionHandlerFactory;
    private final FetchActionService fetchActionService;
    private final FetchRequestMapper fetchRequestMapper;
    private final FetchRequestService fetchRequestService;

    @Async
    public void fetch(FetchRequestDto fetchRequestDto, SseEmitter sseEmitter) {
        try {
            var fetchRequest = fetchRequestMapper.toFetchRequest(fetchRequestDto);
            fetch(fetchRequest, sseEmitter);
            sseEmitter.complete();
        } catch (Exception ex) {
            sseEmitter.completeWithError(ex);
        }
    }

    private void fetch(FetchRequest fetchRequest, SseEmitter sseEmitter) {
        try {
            fetchRequest = fetchRequestService.startFetch(fetchRequest);
            var fetchAction = getFirstFetchAction(fetchRequest);
            while (fetchAction != null) {
                var fetchResult = fetch(fetchAction);
                fetchAction = fetchResult.nextFetchAction();

                var fetchEvent = getFetchEvent(fetchResult);
                sseEmitter.send(SseEvent.named(fetchEvent.type(), fetchEvent));
            }
            fetchRequestService.fetchCompleted(fetchRequest);
        } catch (Exception ex) {
            fetchRequestService.fetchFailed(fetchRequest, ex);
            throw new RuntimeException(ex);
        }
    }

    private FetchResult<?> fetch(FetchAction fetchAction) {
        try {
            fetchAction = fetchActionService.startAction(fetchAction);
            var actionHandler = fetchActionHandlerFactory.getHandlerForAction(fetchAction);
            var fetchResult = actionHandler.fetch(fetchAction);
            fetchActionService.actionCompleted(fetchAction, fetchResult.items().size());
            return fetchResult;
        } catch (Exception ex) {
            fetchActionService.actionFailed(fetchAction, ex);
            throw ex;
        }
    }

    private static FetchAction getFirstFetchAction(FetchRequest fetchRequest) {
        return FetchAction.builder()
                .fetchRequestId(fetchRequest.getId())
                .actionType(FetchAction.ActionType.valueOf(fetchRequest.getFetchType().name()))
                .objectId(fetchRequest.getObjectId())
                .publishedAfter(fetchRequest.getPublishedAfter())
                .publishedBefore(fetchRequest.getPublishedBefore())
                .status(FetchAction.Status.FETCHING)
                .build();
    }

    private static FetchEvent getFetchEvent(FetchResult<?> fetchResult) {
        var type = switch (fetchResult.actionType()) {
            case CHANNELS -> "fetch-channels";
            case VIDEOS -> "fetch-videos";
            case COMMENTS -> "fetch-comments";
            default -> null;
        };
        var status = fetchResult.hasNextFetchAction() ? FetchStatus.FETCHING : FetchStatus.COMPLETED;
        return new FetchEvent(type, fetchResult.objectId(), status, fetchResult.items());
    }
}
