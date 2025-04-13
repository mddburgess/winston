package ca.metricalsky.winston.service.fetch.request;

import ca.metricalsky.winston.entity.fetch.FetchRequest;
import ca.metricalsky.winston.entity.fetch.FetchRequest.FetchType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class FetchRequestHandlerFactoryTest {

    @InjectMocks
    private FetchRequestHandlerFactory fetchRequestHandlerFactory;

    @Mock
    private DefaultFetchRequestHandler defaultFetchRequestHandler;
    @Mock
    private FetchVideoRepliesRequestHandler fetchVideoRepliesRequestHandler;

    @ParameterizedTest
    @EnumSource(
            value = FetchType.class,
            names = { "CHANNELS", "VIDEOS", "COMMENTS" }
    )
    void getHandler(FetchType fetchType) {
        var fetchRequest = FetchRequest.builder()
                .fetchType(fetchType)
                .build();

        var handler = fetchRequestHandlerFactory.getHandler(fetchRequest);

        assertThat(handler).isEqualTo(defaultFetchRequestHandler);
    }

    @Test
    void getHandler_repliesForComment() {
        var fetchRequest = FetchRequest.builder()
                .fetchType(FetchType.REPLIES)
                .mode("FOR_COMMENT")
                .build();

        var handler = fetchRequestHandlerFactory.getHandler(fetchRequest);

        assertThat(handler).isEqualTo(defaultFetchRequestHandler);
    }

    @Test
    void getHandler_repliesForVideo() {
        var fetchRequest = FetchRequest.builder()
                .fetchType(FetchType.REPLIES)
                .mode("FOR_VIDEO")
                .build();

        var handler = fetchRequestHandlerFactory.getHandler(fetchRequest);

        assertThat(handler).isEqualTo(fetchVideoRepliesRequestHandler);
    }
}
