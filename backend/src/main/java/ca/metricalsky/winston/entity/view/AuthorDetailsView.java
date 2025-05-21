package ca.metricalsky.winston.entity.view;

import ca.metricalsky.winston.entity.AuthorEntity;

public interface AuthorDetailsView {

    AuthorEntity getAuthor();

    Long getCommentedVideos();

    Long getTotalComments();

    Long getTotalReplies();
}
