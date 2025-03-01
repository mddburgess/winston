package ca.metricalsky.yt.comments.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;

import static com.google.common.base.MoreObjects.firstNonNull;

@Configuration
public class AppResourceResolver extends PathResourceResolver {

    @Override
    protected Resource getResource(String resourcePath, Resource location) throws IOException {
        return firstNonNull(
                super.getResource(resourcePath, location),
                new ClassPathResource("/static/index.html")
        );
    }
}
