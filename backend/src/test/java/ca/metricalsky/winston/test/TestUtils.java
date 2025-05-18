package ca.metricalsky.winston.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.Base64;

public final class TestUtils {

    private TestUtils() {

    }

    public static boolean randomBoolean() {
        return RandomUtils.secure().randomBoolean();
    }

    public static byte[] randomBytes(int length) {
        return RandomUtils.secure().randomBytes(length);
    }

    public static String randomId() {
        return Base64.getUrlEncoder().encodeToString(randomBytes(18));
    }

    public static long randomLong() {
        return RandomUtils.secure().randomLong(0, 1000);
    }

    public static String randomString() {
        return RandomStringUtils.secure().nextAlphabetic(10, 20);
    }
}
