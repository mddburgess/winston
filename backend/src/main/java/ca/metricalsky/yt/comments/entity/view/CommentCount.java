package ca.metricalsky.yt.comments.entity.view;

public interface CommentCount {

    String getVideoId();

    Long getComments();

    Long getReplies();

    class Empty implements CommentCount {

        @Override
        public String getVideoId() {
            return "";
        }

        @Override
        public Long getComments() {
            return 0L;
        }

        @Override
        public Long getReplies() {
            return 0L;
        }
    }
}
