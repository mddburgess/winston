package ca.metricalsky.winston.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.security.SecureRandom;
import java.util.Base64;

public final class TestUtils {

    private static final SecureRandom RANDOM = new SecureRandom();

    private TestUtils() {

    }

    public static String randomId() {
        var bytes = new byte[18];
        RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().encodeToString(bytes);
    }

    public static long randomLong() {
        return RandomUtils.secure().randomLong(0, 1000);
    }

    public static String randomString() {
        return RandomStringUtils.secure().nextAlphabetic(10, 20);
    }
}
