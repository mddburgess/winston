package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.client.YouTubeClientAdapter;
import ca.metricalsky.winston.dto.CommentDto;
import ca.metricalsky.winston.entity.fetch.FetchAction;
import ca.metricalsky.winston.mapper.dto.CommentDtoMapper;
import ca.metricalsky.winston.mapper.entity.CommentMapper;
import ca.metricalsky.winston.service.CommentService;
import com.google.api.services.youtube.model.CommentThreadListResponse;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FetchCommentsActionHandler implements FetchActionHandler<CommentDto> {

    private final CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);
    private final CommentDtoMapper commentDtoMapper = Mappers.getMapper(CommentDtoMapper.class);
    private final CommentService commentService;
    private final YouTubeClientAdapter youTubeClientAdapter;

    @Override
    public FetchResult<CommentDto> fetch(FetchAction fetchAction) {
        var commentThreadListResponse = youTubeClientAdapter.getComments(fetchAction);
        var commentEntities = commentThreadListResponse.getItems()
                .stream()
                .map(commentMapper::fromYouTube)
                .toList();
        commentService.saveAll(commentEntities);
        var commentDtos = commentEntities.stream()
                .map(commentDtoMapper::fromEntity)
                .toList();
        var nextFetchAction = getNextFetchAction(fetchAction, commentThreadListResponse);
        return new FetchResult<>(fetchAction, commentDtos, nextFetchAction);
    }

    private FetchAction getNextFetchAction(FetchAction fetchAction, CommentThreadListResponse youTubeResponse) {
        return youTubeResponse.getNextPageToken() == null ? null : FetchAction.builder()
                .fetchRequestId(fetchAction.getFetchRequestId())
                .actionType(fetchAction.getActionType())
                .objectId(fetchAction.getObjectId())
                .pageToken(youTubeResponse.getNextPageToken())
                .build();
    }
}
