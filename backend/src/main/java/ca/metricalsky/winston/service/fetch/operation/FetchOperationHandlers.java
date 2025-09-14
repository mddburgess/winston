package ca.metricalsky.winston.service.fetch.operation;

import ca.metricalsky.winston.api.model.Channel;
import ca.metricalsky.winston.api.model.Comment;
import ca.metricalsky.winston.api.model.TopLevelComment;
import ca.metricalsky.winston.api.model.Video;
import ca.metricalsky.winston.service.fetch.FetchOperationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class FetchOperationHandlers {

    @Bean
    FetchOperationHandler<Channel> fetchChannelOperationHandler(
            FetchOperationService fetchOperationService,
            BasicFetchOperation<Channel> fetchChannelOperation
    ) {
        return new FetchOperationHandler<>(fetchOperationService, fetchChannelOperation);
    }

    @Bean
    FetchOperationHandler<Video> fetchVideosOperationHandler(
            FetchOperationService fetchOperationService,
            BasicFetchOperation<Video> fetchVideoOperation
    ) {
        return new FetchOperationHandler<>(fetchOperationService, fetchVideoOperation);
    }

    @Bean
    FetchOperationHandler<TopLevelComment> fetchCommentsOperationHandler(
            FetchOperationService fetchOperationService,
            FetchCommentsOperation fetchCommentsOperation
    ) {
        return new FetchOperationHandler<>(fetchOperationService, fetchCommentsOperation);
    }

    @Bean
    FetchOperationHandler<Comment> fetchVideoRepliesOperationHandler(
            FetchOperationService fetchOperationService,
            FetchVideoRepliesOperation fetchVideoRepliesOperation
    ) {
        return new FetchOperationHandler<>(fetchOperationService, fetchVideoRepliesOperation);
    }

    @Bean
    FetchOperationHandler<Comment> fetchCommentRepliesOperationHandler(
            FetchOperationService fetchOperationService,
            BasicFetchOperation<Comment> fetchCommentRepliesOperation
    ) {
        return new FetchOperationHandler<>(fetchOperationService, fetchCommentRepliesOperation);
    }
}
