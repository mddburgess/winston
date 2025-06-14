package ca.metricalsky.winston.service.fetch.action;

import ca.metricalsky.winston.api.model.Comment;
import ca.metricalsky.winston.dao.CommentDataService;
import ca.metricalsky.winston.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.service.YouTubeService;
import ca.metricalsky.winston.service.fetch.FetchActionService;
import ca.metricalsky.winston.service.fetch.FetchResult;
import com.google.api.services.youtube.model.CommentListResponse;
import org.springframework.stereotype.Service;

@Service
public class FetchRepliesActionHandler extends FetchActionHandler<Comment> {

    private final CommentDataService commentDataService;
    private final YouTubeService youTubeService;

    public FetchRepliesActionHandler(
            FetchActionService fetchActionService,
            CommentDataService commentDataService,
            YouTubeService youTubeService
    ) {
        super(fetchActionService);
        this.commentDataService = commentDataService;
        this.youTubeService = youTubeService;
    }

    @Override
    protected FetchResult<Comment> doFetch(FetchActionEntity fetchAction) {
        var commentListResponse = youTubeService.getReplies(fetchAction);
        var replies = commentDataService.saveReplies(fetchAction.getObjectId(), commentListResponse);
        var nextFetchAction = getNextFetchAction(fetchAction, commentListResponse);

        return new FetchResult<>(fetchAction, replies, nextFetchAction);
    }

    private static FetchActionEntity getNextFetchAction(FetchActionEntity fetchAction, CommentListResponse youTubeResponse) {
        return youTubeResponse.getNextPageToken() == null ? null : FetchActionEntity.builder()
                .fetchOperationId(fetchAction.getFetchOperationId())
                .actionType(fetchAction.getActionType())
                .objectId(fetchAction.getObjectId())
                .pageToken(youTubeResponse.getNextPageToken())
                .build();
    }
}
