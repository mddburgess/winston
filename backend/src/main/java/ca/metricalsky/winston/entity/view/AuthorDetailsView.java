package ca.metricalsky.winston.entity.view;

import ca.metricalsky.winston.entity.AuthorEntity;
import lombok.Value;


@Value
public class AuthorDetailsView {

    public AuthorDetailsView(
            AuthorEntity authorEntity,
            Long channelCount,
            Long videoCount,
            Long totalCommentCount,
            Long replyCount
    ) {
        this.author = authorEntity;
        this.channelCount = channelCount;
        this.videoCount = videoCount;
        this.commentCount = totalCommentCount - replyCount;
        this.replyCount = replyCount;
    }

    AuthorEntity author;
    Long channelCount;
    Long videoCount;
    Long commentCount;
    Long replyCount;
}
