package ca.metricalsky.winston.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationsServiceTest {

    private final NotificationsService notificationsService = new NotificationsService();

    @Test
    void openSubscription() {
        var ssePublisher = notificationsService.openSubscription();

        assertThat(ssePublisher)
                .hasNoNullFieldsOrProperties();
        assertThat(notificationsService.requireSubscription(ssePublisher.getId()))
                .isEqualTo(ssePublisher);
    }

    @Test
    void start() {
        notificationsService.start();

        assertThat(notificationsService.isRunning()).isTrue();
    }

    @Test
    void stop() {
        notificationsService.start();
        notificationsService.openSubscription();
        notificationsService.stop();

        assertThat(notificationsService.isRunning()).isFalse();
    }

    @Test
    void stop_emptySubscriptions() {
        notificationsService.start();
        notificationsService.stop();

        assertThat(notificationsService.isRunning()).isFalse();
    }
}
