package ca.metricalsky.winston.service.fetch.action;

import ca.metricalsky.winston.api.model.Channel;
import ca.metricalsky.winston.api.model.Comment;
import ca.metricalsky.winston.api.model.TopLevelComment;
import ca.metricalsky.winston.api.model.Video;
import ca.metricalsky.winston.event.EventPublisherService;
import ca.metricalsky.winston.service.fetch.FetchActionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class FetchActionHandlers {

    @Bean
    FetchActionHandler<Channel> fetchChannelActionHandler(
            EventPublisherService eventPublisherService,
            FetchActionService fetchActionService,
            FetchChannelAction fetchChannelAction
    ) {
        return new FetchActionHandler<>(eventPublisherService, fetchActionService, fetchChannelAction);
    }

    @Bean
    FetchActionHandler<Video> fetchVideosActionHandler(
            EventPublisherService eventPublisherService,
            FetchActionService fetchActionService,
            FetchVideosAction fetchVideosAction
    ) {
        return new FetchActionHandler<>(eventPublisherService, fetchActionService, fetchVideosAction);
    }

    @Bean
    FetchActionHandler<TopLevelComment> fetchCommentsActionHandler(
            EventPublisherService eventPublisherService,
            FetchActionService fetchActionService,
            FetchCommentsAction fetchCommentsAction
    ) {
        return new FetchActionHandler<>(eventPublisherService, fetchActionService, fetchCommentsAction);
    }

    @Bean
    FetchActionHandler<Comment> fetchRepliesActionHandler(
            EventPublisherService eventPublisherService,
            FetchActionService fetchActionService,
            FetchRepliesAction fetchRepliesAction
    ) {
        return new FetchActionHandler<>(eventPublisherService, fetchActionService, fetchRepliesAction);
    }
}
