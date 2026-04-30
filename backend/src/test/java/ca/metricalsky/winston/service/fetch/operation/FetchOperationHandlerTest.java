package ca.metricalsky.winston.service.fetch.operation;

import ca.metricalsky.winston.database.entity.fetch.FetchOperationEntity;
import ca.metricalsky.winston.exception.FetchOperationException;
import ca.metricalsky.winston.service.fetch.FetchOperationService;
import ca.metricalsky.winston.test.annotations.UnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@UnitTest
class FetchOperationHandlerTest {

    @InjectMocks
    private FetchOperationHandler<?> fetchOperationHandler;

    @Mock
    private FetchOperationService fetchOperationService;
    @Mock
    private FetchOperation<?> fetchOperation;

    private final FetchOperationEntity fetchOperationEntity = new FetchOperationEntity();

    @BeforeEach
    void beforeEach() {
        doAnswer(returnsFirstArg())
                .when(fetchOperationService).startFetch(fetchOperationEntity);
    }

    @Test
    void fetch() {
        doAnswer(returnsFirstArg())
                .when(fetchOperationService).fetchSuccessful(fetchOperationEntity);

        fetchOperationHandler.fetch(fetchOperationEntity);

        verify(fetchOperation)
                .afterFetch(fetchOperationEntity);
    }

    @Test
    void fetch_throwsFetchOperationException() {
        var cause = new RuntimeException();
        var exception = new FetchOperationException(cause);

        doThrow(exception)
                .when(fetchOperation).fetch(fetchOperationEntity);
        doAnswer(returnsFirstArg())
                .when(fetchOperationService).fetchWarning(fetchOperationEntity, cause);

        fetchOperationHandler.fetch(fetchOperationEntity);

        verify(fetchOperation)
                .afterFetch(fetchOperationEntity);
    }

    @Test
    void fetch_throwsRuntimeException() {
        var exception = new RuntimeException();

        doThrow(exception)
                .when(fetchOperation).fetch(fetchOperationEntity);
        doAnswer(returnsFirstArg())
                .when(fetchOperationService).fetchFailed(fetchOperationEntity, exception);

        assertThatThrownBy(() -> fetchOperationHandler.fetch(fetchOperationEntity))
                .isEqualTo(exception);

        verify(fetchOperation)
                .afterFetch(fetchOperationEntity);
    }
}
