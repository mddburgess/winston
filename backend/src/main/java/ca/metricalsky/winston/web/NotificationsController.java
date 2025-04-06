package ca.metricalsky.winston.web;

import ca.metricalsky.winston.service.NotificationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class NotificationsController {

    private final NotificationsService notificationsService;

    @Autowired
    public NotificationsController(NotificationsService notificationsService) {
        this.notificationsService = notificationsService;
    }

    @GetMapping("/api/notifications")
    public SseEmitter subscribe() {
        return notificationsService.openSubscription().getSseEmitter();
    }
}
