package ca.metricalsky.winston.test;

import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestResources {

    private static final Path ROOT_DIR = Paths.get("src", "test", "resources");
    private final Path resourceDir;

    private TestResources(Path resourceDir) {
        this.resourceDir = resourceDir;
    }

    public static TestResources dir(String first, String... more) {
        return new TestResources(ROOT_DIR.resolve(first, more));
    }

    public String load(String first, String... more) throws IOException {
        return FileUtils.readFileToString(resourceDir.resolve(first, more).toFile(), StandardCharsets.UTF_8);
    }
}
