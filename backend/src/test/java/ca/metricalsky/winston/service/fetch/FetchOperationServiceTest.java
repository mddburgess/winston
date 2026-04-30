package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.database.entity.fetch.FetchOperationEntity;
import ca.metricalsky.winston.database.entity.fetch.FetchOperationEntity.Status;
import ca.metricalsky.winston.events.EventPublisher;
import ca.metricalsky.winston.database.repository.fetch.FetchOperationRepository;
import ca.metricalsky.winston.test.annotations.UnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

@UnitTest
class FetchOperationServiceTest {

    @InjectMocks
    private FetchOperationService fetchOperationService;

    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private FetchOperationRepository fetchOperationRepository;

    private final FetchOperationEntity fetchOperationEntity = new FetchOperationEntity();;

    @BeforeEach
    void beforeEach() {
        doAnswer(returnsFirstArg())
                .when(fetchOperationRepository).save(fetchOperationEntity);
    }

    @Test
    void startFetch() {
        var result = fetchOperationService.startFetch(fetchOperationEntity);

        assertThat(result.getStatus())
                .isEqualTo(Status.FETCHING);
        assertThat(result.getError())
                .isNull();
        verify(eventPublisher)
                .publishEvent(result, null);
    }

    @Test
    void fetchSuccessful() {
        var result = fetchOperationService.fetchSuccessful(fetchOperationEntity);

        assertThat(result.getStatus())
                .isEqualTo(Status.SUCCESSFUL);
        assertThat(result.getError())
                .isNull();
        verify(eventPublisher)
                .publishEvent(result, null);
    }

    @Test
    void fetchWarning() {
        var exception = new RuntimeException();

        var result = fetchOperationService.fetchWarning(fetchOperationEntity, exception);

        assertThat(result.getStatus())
                .isEqualTo(Status.WARNING);
        assertThat(result.getError())
                .isNotNull();
    }

    @Test
    void fetchFailed() {
        var exception = new RuntimeException();

        var result = fetchOperationService.fetchFailed(fetchOperationEntity, exception);

        assertThat(result.getStatus())
                .isEqualTo(Status.FAILED);
        assertThat(result.getError())
                .isNotNull();
    }
}
