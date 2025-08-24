package ca.metricalsky.winston.service.fetch.operation;

import ca.metricalsky.winston.api.model.Channel;
import ca.metricalsky.winston.api.model.Comment;
import ca.metricalsky.winston.api.model.Video;
import ca.metricalsky.winston.service.fetch.action.FetchActionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class FetchOperations {

    @Bean
    BasicFetchOperation<Channel> fetchChannelOperation(
            FetchActionHandler<Channel> fetchChannelActionHandler
    ) {
        return new BasicFetchOperation<>(fetchChannelActionHandler);
    }

    @Bean
    BasicFetchOperation<Video> fetchVideosOperation(
            FetchActionHandler<Video> fetchVideoActionHandler
    ) {
        return new BasicFetchOperation<>(fetchVideoActionHandler);
    }

    @Bean
    BasicFetchOperation<Comment> fetchCommentRepliesOperation(
            FetchActionHandler<Comment> fetchRepliesActionHandler
    ) {
        return new BasicFetchOperation<>(fetchRepliesActionHandler);
    }
}
