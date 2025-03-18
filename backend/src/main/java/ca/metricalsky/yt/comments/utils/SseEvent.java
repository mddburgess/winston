package ca.metricalsky.yt.comments.utils;

import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

import java.util.UUID;

public final class SseEvent {

    private SseEvent() {

    }

    public static SseEventBuilder named(String name, Object data) {
        return SseEmitter.event()
                .id(UUID.randomUUID().toString())
                .name(name)
                .data(data, MediaType.APPLICATION_JSON);
    }
}
