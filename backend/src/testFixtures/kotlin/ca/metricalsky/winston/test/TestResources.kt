package ca.metricalsky.winston.test

import org.junit.jupiter.api.fail
import java.io.IOException
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.readText

class TestResources private constructor(
    private val resourceDir: Path = Path("src", "test", "resources"),
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
        operator fun get(subpath: String) = TestResources()[subpath]
    }
}
