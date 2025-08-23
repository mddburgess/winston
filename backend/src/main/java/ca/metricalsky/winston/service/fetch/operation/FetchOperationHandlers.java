package ca.metricalsky.winston.service.fetch.operation;

import ca.metricalsky.winston.api.model.Channel;
import ca.metricalsky.winston.api.model.Comment;
import ca.metricalsky.winston.api.model.TopLevelComment;
import ca.metricalsky.winston.api.model.Video;
import ca.metricalsky.winston.events.EventPublisher;
import ca.metricalsky.winston.service.fetch.FetchOperationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class FetchOperationHandlers {

    @Bean
    FetchOperationHandler<Channel> fetchChannelOperationHandler(
            EventPublisher eventPublisher,
            FetchOperationService fetchOperationService,
            BasicFetchOperation<Channel> fetchChannelOperation
    ) {
        return new FetchOperationHandler<>(eventPublisher, fetchOperationService, fetchChannelOperation);
    }

    @Bean
    FetchOperationHandler<Video> fetchVideosOperationHandler(
            EventPublisher eventPublisher,
            FetchOperationService fetchOperationService,
            BasicFetchOperation<Video> fetchVideoOperation
    ) {
        return new FetchOperationHandler<>(eventPublisher, fetchOperationService, fetchVideoOperation);
    }

    @Bean
    FetchOperationHandler<TopLevelComment> fetchCommentsOperationHandler(
            EventPublisher eventPublisher,
            FetchOperationService fetchOperationService,
            FetchCommentsOperation fetchCommentsOperation
    ) {
        return new FetchOperationHandler<>(eventPublisher, fetchOperationService, fetchCommentsOperation);
    }

    @Bean
    FetchOperationHandler<Comment> fetchVideoRepliesOperationHandler(
            EventPublisher eventPublisher,
            FetchOperationService fetchOperationService,
            FetchVideoRepliesOperation fetchVideoRepliesOperation
    ) {
        return new FetchOperationHandler<>(eventPublisher, fetchOperationService, fetchVideoRepliesOperation);
    }

    @Bean
    FetchOperationHandler<Comment> fetchCommentRepliesOperationHandler(
            EventPublisher eventPublisher,
            FetchOperationService fetchOperationService,
            BasicFetchOperation<Comment> fetchCommentRepliesOperation
    ) {
        return new FetchOperationHandler<>(eventPublisher, fetchOperationService, fetchCommentRepliesOperation);
    }
}
