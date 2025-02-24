package ca.metricalsky.yt.comments.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontendController {

    @GetMapping({
            "/{x:[\\w\\-]+}",
            "/{x:(?!api$).*}/*/{y:[\\w\\-]+}"
    })
    public String forwardToFrontend() {
        return "/index.html";
    }
}
