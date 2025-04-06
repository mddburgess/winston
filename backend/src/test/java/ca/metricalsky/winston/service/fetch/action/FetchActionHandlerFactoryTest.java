package ca.metricalsky.winston.service.fetch.action;

import ca.metricalsky.winston.entity.fetch.FetchAction;
import ca.metricalsky.winston.exception.AppException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@ExtendWith(MockitoExtension.class)
class FetchActionHandlerFactoryTest {

    @InjectMocks
    private FetchActionHandlerFactory fetchActionHandlerFactory;

    @Mock
    private FetchChannelActionHandler fetchChannelActionHandler;
    @Mock
    private FetchCommentsActionHandler fetchCommentsActionHandler;
    @Mock
    private FetchRepliesActionHandler fetchRepliesActionHandler;
    @Mock
    private FetchVideosActionHandler fetchVideosActionHandler;

    @Test
    void getHandlerForAction_channels() {
        var fetchAction = FetchAction.builder()
                .actionType(FetchAction.ActionType.CHANNELS)
                .build();

        var handler = fetchActionHandlerFactory.getHandlerForAction(fetchAction);

        assertThat(handler).isEqualTo(fetchChannelActionHandler);
    }

    @Test
    void getHandlerForAction_videos() {
        var fetchAction = FetchAction.builder()
                .actionType(FetchAction.ActionType.VIDEOS)
                .build();

        var handler = fetchActionHandlerFactory.getHandlerForAction(fetchAction);

        assertThat(handler).isEqualTo(fetchVideosActionHandler);
    }

    @Test
    void getHandlerForAction_comments() {
        var fetchAction = FetchAction.builder()
                .actionType(FetchAction.ActionType.COMMENTS)
                .build();

        var handler = fetchActionHandlerFactory.getHandlerForAction(fetchAction);

        assertThat(handler).isEqualTo(fetchCommentsActionHandler);
    }

    @Test
    void getHandlerForAction_replies() {
        var fetchAction = FetchAction.builder()
                .actionType(FetchAction.ActionType.REPLIES)
                .build();

        var handler = fetchActionHandlerFactory.getHandlerForAction(fetchAction);

        assertThat(handler).isEqualTo(fetchRepliesActionHandler);
    }

    @Test
    void getHandlerForAction_unsupported() {
        var fetchAction = new FetchAction();

        assertThatThrownBy(() -> fetchActionHandlerFactory.getHandlerForAction(fetchAction))
                .isExactlyInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST)
                .hasFieldOrPropertyWithValue("body.detail", "The fetch action must have a valid action type.");
    }
}
