package ca.metricalsky.winston.utils;

import com.google.common.annotations.VisibleForTesting;

import java.time.Clock;
import java.time.OffsetDateTime;

public final class CurrentTime {

    private static Clock clock = Clock.systemDefaultZone();

    private CurrentTime() {

    }

    public static OffsetDateTime asOffsetDateTime() {
        return OffsetDateTime.now(clock);
    }

    @VisibleForTesting
    public static void mock(String time) {
        var offsetDateTime = OffsetDateTime.parse(time);
        clock = Clock.fixed(offsetDateTime.toInstant(), offsetDateTime.getOffset());
    }

    @VisibleForTesting
    public static void reset() {
        clock = Clock.systemDefaultZone();
    }
}
