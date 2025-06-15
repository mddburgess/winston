package ca.metricalsky.winston.test;

import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.model.Activity;
import com.google.api.services.youtube.model.ActivityContentDetails;
import com.google.api.services.youtube.model.ActivityContentDetailsPlaylistItem;
import com.google.api.services.youtube.model.ActivityContentDetailsUpload;
import com.google.api.services.youtube.model.ActivityListResponse;
import com.google.api.services.youtube.model.ActivitySnippet;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.Comment;
import com.google.api.services.youtube.model.CommentListResponse;
import com.google.api.services.youtube.model.CommentSnippet;
import com.google.api.services.youtube.model.CommentThread;
import com.google.api.services.youtube.model.CommentThreadListResponse;
import com.google.api.services.youtube.model.CommentThreadSnippet;

import java.util.List;

public final class ClientTestObjectFactory {

    private ClientTestObjectFactory() {

    }

    public static ChannelListResponse buildChannelListResponse() {
        var channelListResponse = new ChannelListResponse();
        channelListResponse.setItems(List.of(buildChannel()));
        return channelListResponse;
    }

    private static Channel buildChannel() {
        var channel = new Channel();
        channel.setId(TestUtils.randomId());
        return channel;
    }

    public static ActivityListResponse buildActivityListResponse() {
        var activityListResponse = new ActivityListResponse();
        activityListResponse.setItems(List.of(buildUploadActivity(), buildPlaylistItemActivity()));
        return activityListResponse;
    }

    private static Activity buildUploadActivity() {
        var upload = new ActivityContentDetailsUpload();
        upload.setVideoId(TestUtils.randomId());

        var contentDetails = new ActivityContentDetails();
        contentDetails.setUpload(upload);

        return buildActivity(contentDetails);
    }

    private static Activity buildPlaylistItemActivity() {
        var playlistItem = new ActivityContentDetailsPlaylistItem();
        playlistItem.setPlaylistId(TestUtils.randomId());

        var contentDetails = new ActivityContentDetails();
        contentDetails.setPlaylistItem(playlistItem);

        return buildActivity(contentDetails);
    }

    private static Activity buildActivity(ActivityContentDetails contentDetails) {
        var snippet = new ActivitySnippet();
        snippet.setPublishedAt(new DateTime("2025-01-01T00:00:00Z"));

        var activity = new Activity();
        activity.setContentDetails(contentDetails);
        activity.setSnippet(snippet);
        return activity;
    }

    public static CommentThreadListResponse buildCommentThreadListResponse() {
        var commentThreadListResponse = new CommentThreadListResponse();
        commentThreadListResponse.setItems(List.of(buildCommentThread()));
        return commentThreadListResponse;
    }

    private static CommentThread buildCommentThread() {
        var topLevelComment = new Comment();
        topLevelComment.setId(TestUtils.randomId());

        var snippet = new CommentThreadSnippet();
        snippet.setTopLevelComment(topLevelComment);

        var commentThread = new CommentThread();
        commentThread.setSnippet(snippet);
        return commentThread;
    }

    public static CommentListResponse buildCommentListResponse() {
        var commentListResponse = new CommentListResponse();
        commentListResponse.setItems(List.of(buildComment()));
        return commentListResponse;
    }

    private static Comment buildComment() {
        var snippet = new CommentSnippet();
        snippet.setParentId(TestUtils.randomId());

        var comment = new Comment();
        comment.setId(TestUtils.randomId());
        comment.setSnippet(snippet);
        return comment;
    }
}
