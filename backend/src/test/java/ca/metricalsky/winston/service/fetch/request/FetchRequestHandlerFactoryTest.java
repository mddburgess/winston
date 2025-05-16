package ca.metricalsky.winston.service.fetch.request;

import ca.metricalsky.winston.entity.fetch.FetchRequestEntity;
import ca.metricalsky.winston.entity.fetch.FetchRequestEntity.FetchType;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@ExtendWith(MockitoExtension.class)
class FetchRequestHandlerFactoryTest {

    @InjectMocks
    private FetchRequestHandlerFactory fetchRequestHandlerFactory;

    @Mock
    private FetchChannelsRequestHandler fetchChannelsRequestHandler;
    @Mock
    private FetchCommentRepliesRequestHandler fetchCommentRepliesRequestHandler;
    @Mock
    private FetchCommentsRequestHandler fetchCommentsRequestHandler;
    @Mock
    private FetchVideoRepliesRequestHandler fetchVideoRepliesRequestHandler;
    @Mock
    private FetchVideosRequestHandler fetchVideosRequestHandler;

    @ParameterizedTest(name = "fetchType={0}, mode={1}")
    @MethodSource
    void getHandler(FetchType fetchType, String mode, Class<FetchRequestHandler> expectedFetchRequestHandlerType) {
        var fetchRequest = FetchRequestEntity.builder()
                .fetchType(fetchType)
                .mode(mode)
                .build();

        var handler = fetchRequestHandlerFactory.getHandler(fetchRequest);

        assertThat(handler)
                .isInstanceOf(expectedFetchRequestHandlerType);
    }

    private static List<Arguments> getHandler() {
        return List.of(
                arguments(FetchType.CHANNELS, null, FetchChannelsRequestHandler.class),
                arguments(FetchType.VIDEOS, null, FetchVideosRequestHandler.class),
                arguments(FetchType.COMMENTS, null, FetchCommentsRequestHandler.class),
                arguments(FetchType.REPLIES, "FOR_COMMENT", FetchCommentRepliesRequestHandler.class),
                arguments(FetchType.REPLIES, "FOR_VIDEO", FetchVideoRepliesRequestHandler.class)
        );
    }
}
