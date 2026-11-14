package ca.metricalsky.winston.test

import org.junit.jupiter.api.fail
import java.io.IOException
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.readText

class TestResources private constructor(
    private val resourceDir: Path = rootDir,
) {
    operator fun get(subpath: String) = TestResources(resourceDir.resolve(subpath))

    fun load(filename: String): String {
        val resourcePath = resourceDir.resolve(filename)
        return try {
            resourcePath.readText()
        } catch (ex: IOException) {
            fail("Failed to load test resource: $resourcePath", ex)
        }
    }

    companion object {
        operator fun get(subPath: String) = TestResources()[subPath]
    }

}

private val rootDir = Path("src", "test", "resources")
