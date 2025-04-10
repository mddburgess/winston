package ca.metricalsky.winston.entity.view;

import ca.metricalsky.winston.entity.Author;

public interface AuthorDetails {

    Author getAuthor();

    Long getCommentedVideos();

    Long getTotalComments();

    Long getTotalReplies();
}
