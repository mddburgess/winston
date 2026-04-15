package ca.metricalsky.winston.service.fetch.action;

import ca.metricalsky.winston.dao.CommentDataService;
import ca.metricalsky.winston.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.entity.fetch.FetchActionEntity.Type;
import ca.metricalsky.winston.service.YouTubeService;
import ca.metricalsky.winston.test.faker.WinstonFaker;
import com.google.api.services.youtube.model.CommentListResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FetchRepliesActionTest {

    private static final WinstonFaker faker = new WinstonFaker();

    @InjectMocks
    private FetchRepliesAction fetchRepliesAction;

    @Mock
    private CommentDataService commentDataService;
    @Mock
    private YouTubeService youTubeService;

    @ParameterizedTest
    @MethodSource
    void fetch(CommentListResponse commentListResponse) {
        var fetchAction = FetchActionEntity.builder()
                .actionType(Type.REPLIES)
                .objectId(faker.youtube().commentId())
                .build();

        when(youTubeService.getReplies(fetchAction))
                .thenReturn(commentListResponse);

        var replies = faker.comment().list();
        when(commentDataService.saveReplies(fetchAction.getObjectId(), commentListResponse))
                .thenReturn(replies);

        var fetchResult = fetchRepliesAction.fetch(fetchAction);

        assertThat(fetchResult)
                .as("fetchResult")
                .hasFieldOrPropertyWithValue("actionType", fetchAction.getActionType())
                .hasFieldOrPropertyWithValue("objectId", fetchAction.getObjectId())
                .hasFieldOrPropertyWithValue("items", replies)
                .hasFieldOrPropertyWithValue("nextFetchAction", null);
    }

    private static List<CommentListResponse> fetch() {
        return List.of(
                faker.youtube().response().commentList().emptyPage(),
                faker.youtube().response().commentList().lastPage()
        );
    }

    @Test
    void fetch_withNextPageToken() {
        var fetchAction = FetchActionEntity.builder()
                .actionType(Type.REPLIES)
                .objectId(faker.youtube().commentId())
                .build();

        var commentListResponse = faker.youtube().response().commentList().firstPage();
        when(youTubeService.getReplies(fetchAction))
                .thenReturn(commentListResponse);

        var replies = faker.comment().list();
        when(commentDataService.saveReplies(fetchAction.getObjectId(), commentListResponse))
                .thenReturn(replies);

        var fetchResult = fetchRepliesAction.fetch(fetchAction);

        assertThat(fetchResult)
                .as("fetchResult")
                .hasFieldOrPropertyWithValue("actionType", fetchAction.getActionType())
                .hasFieldOrPropertyWithValue("objectId", fetchAction.getObjectId())
                .hasFieldOrPropertyWithValue("items", replies);
        assertThat(fetchResult.nextFetchAction())
                .as("fetchResult.nextFetchAction")
                .hasFieldOrPropertyWithValue("fetchOperationId", fetchAction.getFetchOperationId())
                .hasFieldOrPropertyWithValue("actionType", fetchAction.getActionType())
                .hasFieldOrPropertyWithValue("objectId", fetchAction.getObjectId())
                .hasFieldOrPropertyWithValue("pageToken", commentListResponse.getNextPageToken());
    }

    @Test
    void fetch_pageTokenDidNotChange() {
        var pageToken = faker.youtube().response().commentList().nextPageToken();

        var fetchAction = FetchActionEntity.builder()
                .actionType(Type.REPLIES)
                .objectId(faker.youtube().commentId())
                .pageToken(pageToken)
                .build();

        var commentListResponse = faker.youtube().response().commentList().firstPage();
        commentListResponse.setNextPageToken(pageToken);
        when(youTubeService.getReplies(fetchAction))
                .thenReturn(commentListResponse);

        var replies = faker.comment().list();
        when(commentDataService.saveReplies(fetchAction.getObjectId(), commentListResponse))
                .thenReturn(replies);

        var fetchResult = fetchRepliesAction.fetch(fetchAction);

        assertThat(fetchResult)
                .as("fetchResult")
                .hasFieldOrPropertyWithValue("actionType", fetchAction.getActionType())
                .hasFieldOrPropertyWithValue("objectId", fetchAction.getObjectId())
                .hasFieldOrPropertyWithValue("items", replies)
                .hasFieldOrPropertyWithValue("nextFetchAction", null);
    }
}
