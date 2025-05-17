package ca.metricalsky.winston.test;

import org.apache.commons.lang3.RandomStringUtils;

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

    public static String randomString() {
        return RandomStringUtils.secure().nextAlphabetic(10, 20);
    }
}
