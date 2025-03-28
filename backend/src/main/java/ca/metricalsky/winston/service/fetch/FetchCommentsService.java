package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.dto.fetch.FetchRequest;
import ca.metricalsky.winston.events.FetchCommentsEvent;
import ca.metricalsky.winston.events.FetchStatus;
import ca.metricalsky.winston.mapper.dto.CommentDtoMapper;
import ca.metricalsky.winston.repository.CommentRepository;
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

    @Override
    public void fetch(FetchRequest fetchRequest, SseEmitter sseEmitter) {
        var context = buildFetchContext(fetchRequest);

        try {
            do {
                var eventData = fetchComments(context);
                sseEmitter.send(SseEvent.named("fetch-comments", eventData));
            } while (context.hasNext());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private FetchCommentsContext buildFetchContext(FetchRequest fetchRequest) {
        return FetchCommentsContext.builder()
                .videoId(fetchRequest.getComments().getVideoId())
                .build();
    }

    private FetchCommentsEvent fetchComments(FetchCommentsContext context) throws IOException {
        try {
            var videoId = context.getVideoId();
            var pageToken = context.getPageToken();

            var fetchCommentsResponse = youTubeService.fetchComments(videoId, pageToken);
            commentService.saveAll(fetchCommentsResponse.comments());

            var commentDtos = fetchCommentsResponse.comments().stream()
                    .map(commentDtoMapper::fromEntity)
                    .toList();

            context.setPageToken(fetchCommentsResponse.nextPageToken());

            var eventStatus = context.hasNext() ? FetchStatus.FETCHING : FetchStatus.COMPLETED;
            var event = new FetchCommentsEvent(videoId, eventStatus, commentDtos);

            return event;
        } catch (Exception ex) {
            throw ex;
        }
    }
}
