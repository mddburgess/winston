package ca.metricalsky.winston.utils;

import org.springframework.lang.NonNull;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.temporal.Temporal;

public final class NumberUtils {

    private NumberUtils() {

    }

    public static int scaleToNow(Integer value, Temporal fromTime, Temporal toTime) {
        if (value == null || value == 0) {
            return 0;
        }
        if (fromTime == null || toTime == null) {
            return value;
        }

        var valueDuration = Duration.between(fromTime, toTime);
        if (valueDuration.getSeconds() == 0) {
            return value;
        }

        var scaledDuration = Duration.between(fromTime, OffsetDateTime.now());
        var scaleFactor = (double) scaledDuration.getSeconds() / valueDuration.getSeconds();
        return (int) (value * scaleFactor);
    }
}
