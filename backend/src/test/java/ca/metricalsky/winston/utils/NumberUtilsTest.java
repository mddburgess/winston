package ca.metricalsky.winston.utils;

import ca.metricalsky.winston.test.annotations.UnitTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.OffsetDateTime;
import java.time.temporal.Temporal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@UnitTest
class NumberUtilsTest {

    private static final Temporal FROM_TIME = OffsetDateTime.parse("2025-01-01T00:00:00.000Z");
    private static final Temporal TO_TIME = OffsetDateTime.parse("2025-01-02T00:00:00.000Z");

    @BeforeAll
    static void beforeAll() {
        CurrentTime.mock("2025-01-03T00:00:00.000Z");
    }

    @AfterAll
    static void afterAll() {
        CurrentTime.reset();
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(ints = 0)
    void scaleToNow_valueIsNullOrZero(Integer value) {
        var result = NumberUtils.scaleToNow(value, null, null);

        assertThat(result)
                .isEqualTo(0);
    }

    @ParameterizedTest
    @MethodSource
    void scaleToNow_timesAreNullOrEqual(Temporal fromTime, Temporal toTime) {
        var result = NumberUtils.scaleToNow(10, fromTime, toTime);

        assertThat(result)
                .isEqualTo(10);
    }

    private static List<Arguments> scaleToNow_timesAreNullOrEqual() {
        return List.of(
                arguments(null, TO_TIME),
                arguments(FROM_TIME, null),
                arguments(FROM_TIME, FROM_TIME)
        );
    }

    @Test
    void scaleToNow() {
        var result = NumberUtils.scaleToNow(10, FROM_TIME, TO_TIME);

        assertThat(result)
                .isEqualTo(20);
    }
}
