package ca.metricalsky.yt.comments.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;

@Configuration
public class AppResourceResolver extends PathResourceResolver {

    @Override
    @Nullable
    protected Resource getResource(String resourcePath, Resource location) throws IOException {
        var resource = super.getResource(resourcePath, location);
        if (resource == null && !resourcePath.startsWith("api")) {
            resource = new ClassPathResource("/static/index.html");
        }
        return resource;
    }
}
