package ca.metricalsky.winston.test;

import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.fail;

public class TestResources {

    private static final Path ROOT_DIR = Paths.get("src", "test", "resources");
    private final Path resourceDir;

    private TestResources(Path resourceDir) {
        this.resourceDir = resourceDir;
    }

    public static TestResources dir(String first, String... more) {
        return new TestResources(ROOT_DIR.resolve(first, more));
    }

    public String load(String first, String... more) {
        var resourcePath = resourceDir.resolve(first, more);
        try {
            return FileUtils.readFileToString(resourcePath.toFile(), StandardCharsets.UTF_8);
        } catch (IOException ex) {
            fail("Failed to load test resource: " + resourcePath, ex);
            return null;
        }
    }
}
