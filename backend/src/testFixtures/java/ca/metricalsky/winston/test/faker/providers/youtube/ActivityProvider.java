package ca.metricalsky.winston.test.faker.providers.youtube;

import ca.metricalsky.winston.test.faker.WinstonFaker;
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.model.Activity;
import com.google.api.services.youtube.model.ActivityContentDetails;
import com.google.api.services.youtube.model.ActivityContentDetailsUpload;
import com.google.api.services.youtube.model.ActivitySnippet;
import net.datafaker.providers.base.AbstractProvider;

import java.util.concurrent.TimeUnit;

public class ActivityProvider
        extends AbstractProvider<WinstonFaker> {

    public ActivityProvider(WinstonFaker faker) {
        super(faker);
    }

    public Activity upload() {
        var videoId = faker.youtube().videoId();

        var upload = new ActivityContentDetailsUpload();
        upload.setVideoId(videoId);

        var contentDetails = new ActivityContentDetails();
        contentDetails.setUpload(upload);

        var snippet = new ActivitySnippet();
        snippet.setPublishedAt(publishedAt());

        var activity = new Activity();
        activity.setContentDetails(contentDetails);
        activity.setSnippet(snippet);
        return activity;
    }

    private DateTime publishedAt() {
        var time = faker.timeAndDate().past(7, TimeUnit.DAYS);
        return new DateTime(time.toEpochMilli());
    }
}
