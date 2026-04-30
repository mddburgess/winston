//package ca.metricalsky.winston.service.fetch.action;
//
//import ca.metricalsky.winston.client.CommentsDisabledException;
//import ca.metricalsky.winston.client.VideoNotFoundException;
//import ca.metricalsky.winston.dao.CommentDataService;
//import ca.metricalsky.winston.database.entity.fetch.FetchActionEntity;
//import ca.metricalsky.winston.exception.FetchOperationException;
//import ca.metricalsky.winston.service.VideoCommentsService;
//import ca.metricalsky.winston.service.YouTubeService;
//import ca.metricalsky.winston.test.faker.WinstonFaker;
//import com.google.api.services.youtube.model.CommentThreadListResponse;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.MethodSource;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class FetchCommentsActionTest {
//
//    private static final WinstonFaker faker = new WinstonFaker();
//
//    @InjectMocks
//    private FetchCommentsAction fetchCommentsAction;
//
//    @Mock
//    private CommentDataService commentDataService;
//    @Mock
//    private VideoCommentsService videoCommentsService;
//    @Mock
//    private YouTubeService youTubeService;
//
//    @ParameterizedTest
//    @MethodSource
//    void fetch(CommentThreadListResponse commentThreadListResponse) {
//        var fetchAction = FetchActionEntity.builder()
//                .actionType(FetchActionEntity.Type.COMMENTS)
//                .objectId(faker.youtube().videoId())
//                .build();
//
//        when(youTubeService.getComments(fetchAction))
//                .thenReturn(commentThreadListResponse);
//
//        var comments = faker.topLevelComment().list();
//        when(commentDataService.saveComments(commentThreadListResponse))
//                .thenReturn(comments);
//
//        var fetchResult = fetchCommentsAction.fetch(fetchAction);
//
//        assertThat(fetchResult)
//                .as("fetchResult")
//                .hasFieldOrPropertyWithValue("actionType", fetchAction.getActionType())
//                .hasFieldOrPropertyWithValue("objectId", fetchAction.getObjectId())
//                .hasFieldOrPropertyWithValue("items", comments)
//                .hasFieldOrPropertyWithValue("nextFetchAction", null);
//    }
//
//    private static List<CommentThreadListResponse> fetch() {
//        return List.of(
//                faker.youtube().response().commentThreadList().emptyPage(),
//                faker.youtube().response().commentThreadList().lastPage()
//        );
//    }
//
//    @Test
//    void fetch_withNextPageToken() {
//        var fetchAction = FetchActionEntity.builder()
//                .actionType(FetchActionEntity.Type.COMMENTS)
//                .objectId(faker.youtube().videoId())
//                .build();
//
//        var commentThreadListResponse = faker.youtube().response().commentThreadList().firstPage();
//        when(youTubeService.getComments(fetchAction))
//                .thenReturn(commentThreadListResponse);
//
//        var comments = faker.topLevelComment().list();
//        when(commentDataService.saveComments(commentThreadListResponse))
//                .thenReturn(comments);
//
//        var fetchResult = fetchCommentsAction.fetch(fetchAction);
//
//        assertThat(fetchResult)
//                .as("fetchResult")
//                .hasFieldOrPropertyWithValue("actionType", fetchAction.getActionType())
//                .hasFieldOrPropertyWithValue("objectId", fetchAction.getObjectId())
//                .hasFieldOrPropertyWithValue("items", comments);
//        assertThat(fetchResult.nextFetchAction())
//                .as("fetchResult.nextFetchAction")
//                .hasFieldOrPropertyWithValue("fetchOperationId", fetchAction.getFetchOperationId())
//                .hasFieldOrPropertyWithValue("actionType", fetchAction.getActionType())
//                .hasFieldOrPropertyWithValue("objectId", fetchAction.getObjectId())
//                .hasFieldOrPropertyWithValue("pageToken", commentThreadListResponse.getNextPageToken());
//    }
//
//    @Test
//    void fetch_pageTokenDidNotChange() {
//        var pageToken = faker.youtube().response().commentList().nextPageToken();
//
//        var fetchAction = FetchActionEntity.builder()
//                .actionType(FetchActionEntity.Type.REPLIES)
//                .objectId(faker.youtube().commentId())
//                .pageToken(pageToken)
//                .build();
//
//        var commentThreadListResponse = faker.youtube().response().commentThreadList().firstPage();
//        commentThreadListResponse.setNextPageToken(pageToken);
//        when(youTubeService.getComments(fetchAction))
//                .thenReturn(commentThreadListResponse);
//
//        var comments = faker.topLevelComment().list();
//        when(commentDataService.saveComments(commentThreadListResponse))
//                .thenReturn(comments);
//
//        var fetchResult = fetchCommentsAction.fetch(fetchAction);
//
//        assertThat(fetchResult)
//                .as("fetchResult")
//                .hasFieldOrPropertyWithValue("actionType", fetchAction.getActionType())
//                .hasFieldOrPropertyWithValue("objectId", fetchAction.getObjectId())
//                .hasFieldOrPropertyWithValue("items", comments)
//                .hasFieldOrPropertyWithValue("nextFetchAction", null);
//    }
//
//    @Test
//    void fetch_videoNotFound() {
//        var fetchAction = FetchActionEntity.builder()
//                .actionType(FetchActionEntity.Type.COMMENTS)
//                .objectId(faker.youtube().videoId())
//                .build();
//
//        when(youTubeService.getComments(fetchAction))
//                .thenThrow(new VideoNotFoundException(null));
//
//        assertThatThrownBy(() -> fetchCommentsAction.fetch(fetchAction))
//                .isInstanceOf(FetchOperationException.class)
//                .cause()
//                .isExactlyInstanceOf(VideoNotFoundException.class)
//                .hasFieldOrPropertyWithValue("status", HttpStatus.NOT_FOUND)
//                .hasMessageEndingWith("The requested video was not found.");
//    }
//
//    @Test
//    void fetch_commentsDisabled() {
//        var fetchAction = FetchActionEntity.builder()
//                .actionType(FetchActionEntity.Type.COMMENTS)
//                .objectId(faker.youtube().videoId())
//                .build();
//
//        when(youTubeService.getComments(fetchAction))
//                .thenThrow(new CommentsDisabledException(null));
//
//        assertThatThrownBy(() -> fetchCommentsAction.fetch(fetchAction))
//                .isInstanceOf(FetchOperationException.class)
//                .cause()
//                .isExactlyInstanceOf(CommentsDisabledException.class)
//                .hasFieldOrPropertyWithValue("status", HttpStatus.UNPROCESSABLE_ENTITY)
//                .hasMessageEndingWith("Comments are disabled for the requested video.");
//
//        verify(videoCommentsService).markVideoCommentsDisabled(fetchAction.getObjectId());
//    }
//}
