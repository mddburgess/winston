package ca.metricalsky.winston.test.faker.providers.youtube;

import ca.metricalsky.winston.test.faker.WinstonFaker;
import com.google.api.services.youtube.model.Activity;
import com.google.api.services.youtube.model.ActivityContentDetails;
import com.google.api.services.youtube.model.ActivityContentDetailsUpload;
import net.datafaker.providers.base.AbstractProvider;

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

        var activity = new Activity();
        activity.setContentDetails(contentDetails);
        return activity;
    }
}
