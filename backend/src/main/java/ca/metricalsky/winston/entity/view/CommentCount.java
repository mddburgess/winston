package ca.metricalsky.winston.entity.view;

public interface CommentCount {

    String getVideoId();

    default Long getComments() {
        return getCommentsAndReplies() - getReplies();
    }

    Long getCommentsAndReplies();

    Long getReplies();

    Long getTotalReplies();

    class Empty implements CommentCount {

        @Override
        public String getVideoId() {
            return "";
        }

        @Override
        public Long getCommentsAndReplies() {
            return 0L;
        }

        @Override
        public Long getReplies() {
            return 0L;
        }

        @Override
        public Long getTotalReplies() {
            return 0L;
        }
    }
}
