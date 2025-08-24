package ca.metricalsky.winston.service.fetch.action;

import ca.metricalsky.winston.api.model.Channel;
import ca.metricalsky.winston.api.model.Comment;
import ca.metricalsky.winston.api.model.TopLevelComment;
import ca.metricalsky.winston.api.model.Video;
import ca.metricalsky.winston.events.EventPublisher;
import ca.metricalsky.winston.service.fetch.FetchActionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class FetchActionHandlers {

    @Bean
    FetchActionHandler<Channel> fetchChannelActionHandler(
            EventPublisher eventPublisher,
            FetchActionService fetchActionService,
            FetchChannelAction fetchChannelAction
    ) {
        return new FetchActionHandler<>(eventPublisher, fetchActionService, fetchChannelAction);
    }

    @Bean
    FetchActionHandler<Video> fetchVideosActionHandler(
            EventPublisher eventPublisher,
            FetchActionService fetchActionService,
            FetchVideosAction fetchVideosAction
    ) {
        return new FetchActionHandler<>(eventPublisher, fetchActionService, fetchVideosAction);
    }

    @Bean
    FetchActionHandler<TopLevelComment> fetchCommentsActionHandler(
            EventPublisher eventPublisher,
            FetchActionService fetchActionService,
            FetchCommentsAction fetchCommentsAction
    ) {
        return new FetchActionHandler<>(eventPublisher, fetchActionService, fetchCommentsAction);
    }

    @Bean
    FetchActionHandler<Comment> fetchRepliesActionHandler(
            EventPublisher eventPublisher,
            FetchActionService fetchActionService,
            FetchRepliesAction fetchRepliesAction
    ) {
        return new FetchActionHandler<>(eventPublisher, fetchActionService, fetchRepliesAction);
    }
}
