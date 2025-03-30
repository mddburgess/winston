package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.dto.fetch.FetchRequestDto;
import ca.metricalsky.winston.entity.fetch.FetchAction.ActionType;
import ca.metricalsky.winston.entity.fetch.FetchRequest;
import ca.metricalsky.winston.events.FetchCommentsEvent;
import ca.metricalsky.winston.events.FetchStatus;
import ca.metricalsky.winston.mapper.dto.CommentDtoMapper;
import ca.metricalsky.winston.service.CommentService;
import ca.metricalsky.winston.utils.SseEvent;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FetchCommentsService implements FetchRequestHandler {

    private final CommentDtoMapper commentDtoMapper = Mappers.getMapper(CommentDtoMapper.class);
    private final YouTubeService youTubeService;
    private final CommentService commentService;
    private final FetchActionService fetchActionService;
    private final FetchRequestService fetchRequestService;

    @Override
    public void fetch(FetchRequest fetchRequest, SseEmitter sseEmitter) {
        try {
            var fetchContext = buildFetchContext(fetchRequest);
            fetchRequest = fetchRequestService.startFetch(fetchRequest);

            do {
                var eventData = fetchComments(fetchContext);
                sseEmitter.send(SseEvent.named("fetch-comments", eventData));
            } while (fetchContext.getPageToken() != null);

            fetchRequestService.fetchCompleted(fetchRequest);
        } catch (Exception ex) {
            fetchRequestService.fetchFailed(fetchRequest, ex);
            throw new RuntimeException(ex);
        }
    }

    private FetchContext buildFetchContext(FetchRequest fetchRequest) {
        return FetchContext.builder()
                .objectId(fetchRequest.getObjectId())
                .fetchRequest(fetchRequest)
                .build();
    }

    private FetchCommentsEvent fetchComments(FetchContext fetchContext) throws IOException {
        var fetchAction = fetchActionService.startAction(fetchContext, ActionType.COMMENTS);

        try {
            var fetchCommentsResponse = youTubeService.fetchComments(fetchAction);
            commentService.saveAll(fetchCommentsResponse.comments());

            var commentDtos = fetchCommentsResponse.comments().stream()
                    .map(commentDtoMapper::fromEntity)
                    .toList();

            fetchContext.setPageToken(fetchCommentsResponse.nextPageToken());

            var eventStatus = fetchContext.getPageToken() != null ? FetchStatus.FETCHING : FetchStatus.COMPLETED;
            var event = new FetchCommentsEvent(fetchContext.getObjectId(), eventStatus, commentDtos);

            fetchActionService.actionCompleted(fetchAction, commentDtos.size());
            return event;
        } catch (Exception ex) {
            fetchActionService.actionFailed(fetchAction, ex);
            throw ex;
        }
    }
}
