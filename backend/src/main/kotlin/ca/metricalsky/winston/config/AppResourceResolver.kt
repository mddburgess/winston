package ca.metricalsky.winston.config

import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.web.servlet.resource.PathResourceResolver

@Configuration
class AppResourceResolver : PathResourceResolver() {

    override fun getResource(resourcePath: String, location: Resource): Resource? {
        val resource = super.getResource(resourcePath, location)
        return when {
            resource != null -> resource
            resourcePath.startsWith("api") -> null
            else -> ClassPathResource("/static/index.html")
        }
    }
}
