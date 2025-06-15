package ca.metricalsky.winston.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ApiSpecController {

    @GetMapping("/api/spec/**")
    public String getApiSpec(HttpServletRequest request) {
        var specPath = request.getRequestURI().split(request.getContextPath() + "/api/spec/");
        if (specPath.length <= 1 || specPath[1].isEmpty()) {
            return "/spec/openapi.yaml";
        } else {
            return "/spec/" + specPath[1];
        }
    }

    @GetMapping("/api/docs")
    public String getApiDocs() {
        return "/docs/index.html";
    }
}
