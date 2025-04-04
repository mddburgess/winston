package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.config.exception.AppProblemDetail;
import ca.metricalsky.winston.dto.fetch.FetchRequestDto;
import ca.metricalsky.winston.entity.fetch.FetchAction;
import ca.metricalsky.winston.entity.fetch.FetchRequest;
import ca.metricalsky.winston.events.FetchEvent;
import ca.metricalsky.winston.events.FetchStatus;
import ca.metricalsky.winston.mapper.entity.FetchRequestMapper;
import ca.metricalsky.winston.utils.SsePublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.ErrorResponseException;

@Service
@RequiredArgsConstructor
public class AsyncFetchService {

    private final FetchActionHandlerFactory fetchActionHandlerFactory;
    private final FetchActionService fetchActionService;
    private final FetchRequestMapper fetchRequestMapper;
    private final FetchRequestService fetchRequestService;

    @Async
    public void fetch(FetchRequestDto fetchRequestDto, SsePublisher ssePublisher) {
        try {
            var fetchRequest = fetchRequestMapper.toFetchRequest(fetchRequestDto);
            fetch(fetchRequest, ssePublisher);
            ssePublisher.complete();
        } catch (RuntimeException ex) {
            ssePublisher.publish(AppProblemDetail.forException(ex));
            ssePublisher.completeWithError(ex);
        }
    }

    private void fetch(FetchRequest fetchRequest, SsePublisher ssePublisher) {
        try {
            fetchRequest = fetchRequestService.startFetch(fetchRequest);
            var fetchAction = getFirstFetchAction(fetchRequest);
            while (fetchAction != null) {
                var fetchResult = fetch(fetchAction);
                fetchAction = fetchResult.nextFetchAction();

                var fetchEvent = getFetchEvent(fetchResult);
                ssePublisher.publish(fetchEvent);
            }
            fetchRequestService.fetchCompleted(fetchRequest);
        } catch (RuntimeException ex) {
            fetchRequestService.fetchFailed(fetchRequest, ex);
            throw ex;
        }
    }

    private FetchResult<?> fetch(FetchAction fetchAction) {
        try {
            fetchAction = fetchActionService.startAction(fetchAction);
            var actionHandler = fetchActionHandlerFactory.getHandlerForAction(fetchAction);
            var fetchResult = actionHandler.fetch(fetchAction);
            fetchActionService.actionCompleted(fetchAction, fetchResult.items().size());
            return fetchResult;
        } catch (RuntimeException ex) {
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
