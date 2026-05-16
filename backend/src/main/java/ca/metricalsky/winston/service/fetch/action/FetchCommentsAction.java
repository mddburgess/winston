package ca.metricalsky.winston.service.fetch.action;

import ca.metricalsky.winston.api.model.TopLevelComment;
import ca.metricalsky.winston.client.CommentsDisabledException;
import ca.metricalsky.winston.client.VideoNotFoundException;
import ca.metricalsky.winston.dao.CommentDataService;
import ca.metricalsky.winston.database.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.exception.FetchOperationException;
import ca.metricalsky.winston.service.VideoCommentsService;
import ca.metricalsky.winston.service.YouTubeService;
import ca.metricalsky.winston.service.fetch.FetchResult;
import com.google.api.services.youtube.model.CommentThreadListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FetchCommentsAction implements FetchAction<TopLevelComment> {

    private final CommentDataService commentDataService;
    private final VideoCommentsService videoCommentsService;
    private final YouTubeService youTubeService;

    @Override
    public FetchResult<TopLevelComment> fetch(FetchActionEntity fetchAction) {
        try {
            var commentThreadListResponse = youTubeService.getComments(fetchAction);
            var comments = commentDataService.saveComments(commentThreadListResponse);
            var nextFetchAction = getNextFetchAction(fetchAction, commentThreadListResponse);

            return new FetchResult<>(fetchAction, comments, nextFetchAction);
        } catch (VideoNotFoundException ex) {
            throw new FetchOperationException(ex);
        } catch (CommentsDisabledException ex) {
            videoCommentsService.markVideoCommentsDisabled(fetchAction.getObjectId());
            throw new FetchOperationException(ex);
        }
    }

    private static FetchActionEntity getNextFetchAction(
            FetchActionEntity fetchAction,
            CommentThreadListResponse youTubeResponse
    ) {
        var nextPageToken = youTubeResponse.getNextPageToken();
        if (nextPageToken == null || nextPageToken.equals(fetchAction.getPageToken())) {
            return null;
        }

        var nextFetchAction = new FetchActionEntity();
        nextFetchAction.setFetchOperationId(fetchAction.getFetchOperationId());
        nextFetchAction.setActionType(fetchAction.getActionType());
        nextFetchAction.setObjectId(fetchAction.getObjectId());
        nextFetchAction.setPageToken(nextPageToken);
        return nextFetchAction;
    }
}
