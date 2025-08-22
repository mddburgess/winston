package ca.metricalsky.winston.service.fetch.action;

import ca.metricalsky.winston.api.model.TopLevelComment;
import ca.metricalsky.winston.client.CommentsDisabledException;
import ca.metricalsky.winston.dao.CommentDataService;
import ca.metricalsky.winston.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.exception.FetchOperationException;
import ca.metricalsky.winston.service.VideoCommentsService;
import ca.metricalsky.winston.service.YouTubeService;
import ca.metricalsky.winston.service.fetch.FetchActionService;
import ca.metricalsky.winston.service.fetch.FetchResult;
import com.google.api.services.youtube.model.CommentThreadListResponse;
import org.springframework.stereotype.Service;

@Service
public class FetchCommentsActionHandler extends FetchActionHandler<TopLevelComment> {

    private final CommentDataService commentDataService;
    private final VideoCommentsService videoCommentsService;
    private final YouTubeService youTubeService;

    public FetchCommentsActionHandler(
            FetchActionService fetchActionService,
            CommentDataService commentDataService,
            VideoCommentsService videoCommentsService,
            YouTubeService youTubeService
    ) {
        super(fetchActionService);
        this.commentDataService = commentDataService;
        this.videoCommentsService = videoCommentsService;
        this.youTubeService = youTubeService;
    }

    @Override
    protected FetchResult<TopLevelComment> doFetch(FetchActionEntity fetchAction) {
        try {
            var commentThreadListResponse = youTubeService.getComments(fetchAction);
            var comments = commentDataService.saveComments(commentThreadListResponse);
            var nextFetchAction = getNextFetchAction(fetchAction, commentThreadListResponse);

            return new FetchResult<>(fetchAction, comments, nextFetchAction);
        } catch (CommentsDisabledException ex) {
            videoCommentsService.markVideoCommentsDisabled(fetchAction.getObjectId());
            throw new FetchOperationException(ex);
        }
    }

    private static FetchActionEntity getNextFetchAction(FetchActionEntity fetchAction, CommentThreadListResponse youTubeResponse) {
        return youTubeResponse.getNextPageToken() == null ? null : FetchActionEntity.builder()
                .fetchOperationId(fetchAction.getFetchOperationId())
                .actionType(fetchAction.getActionType())
                .objectId(fetchAction.getObjectId())
                .pageToken(youTubeResponse.getNextPageToken())
                .build();
    }
}
