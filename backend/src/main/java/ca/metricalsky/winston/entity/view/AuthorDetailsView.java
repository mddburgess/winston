package ca.metricalsky.winston.entity.view;

public interface AuthorDetailsView {

    String getAuthorId();

    Long getChannelCount();

    Long getVideoCount();

    Long getTotalCommentCount();

    Long getReplyCount();

    default Long getCommentCount() {
        return getTotalCommentCount() - getReplyCount();
    }
}
