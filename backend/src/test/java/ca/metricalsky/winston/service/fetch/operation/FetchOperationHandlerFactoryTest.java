package ca.metricalsky.winston.service.fetch.operation;

import ca.metricalsky.winston.entity.fetch.FetchOperationEntity;
import ca.metricalsky.winston.entity.fetch.FetchOperationEntity.Type;
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
class FetchOperationHandlerFactoryTest {

    @InjectMocks
    private FetchOperationHandlerFactory fetchOperationHandlerFactory;

    @Mock
    private FetchChannelsOperationHandler fetchChannelsRequestHandler;
    @Mock
    private FetchCommentRepliesOperationHandler fetchCommentRepliesRequestHandler;
    @Mock
    private FetchCommentsOperationHandler fetchCommentsRequestHandler;
    @Mock
    private FetchVideoRepliesOperationHandler fetchVideoRepliesRequestHandler;
    @Mock
    private FetchVideosOperationHandler fetchVideosRequestHandler;

    @ParameterizedTest(name = "fetchType={0}, mode={1}")
    @MethodSource
    void getHandler(Type fetchType, String mode, Class<FetchOperationHandler> expectedFetchRequestHandlerType) {
        var operation = FetchOperationEntity.builder()
                .operationType(fetchType)
                .mode(mode)
                .build();

        var handler = fetchOperationHandlerFactory.getHandler(operation);

        assertThat(handler)
                .isInstanceOf(expectedFetchRequestHandlerType);
    }

    private static List<Arguments> getHandler() {
        return List.of(
                arguments(Type.CHANNELS, null, FetchChannelsOperationHandler.class),
                arguments(Type.VIDEOS, null, FetchVideosOperationHandler.class),
                arguments(Type.COMMENTS, null, FetchCommentsOperationHandler.class),
                arguments(Type.REPLIES, "FOR_COMMENT", FetchCommentRepliesOperationHandler.class),
                arguments(Type.REPLIES, "FOR_VIDEO", FetchVideoRepliesOperationHandler.class)
        );
    }
}
