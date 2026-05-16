package ca.metricalsky.winston.test.faker.providers.youtube;

import ca.metricalsky.winston.test.faker.WinstonFaker;
import com.google.api.services.youtube.model.Activity;
import com.google.api.services.youtube.model.ActivityListResponse;
import net.datafaker.providers.base.AbstractProvider;

import java.util.List;

public class ActivityListResponseProvider
        extends AbstractProvider<WinstonFaker> {

    public ActivityListResponseProvider(WinstonFaker faker) {
        super(faker);
    }

    public ActivityListResponse emptyPage() {
        return page(0, 0, null);
    }

    public ActivityListResponse firstPage() {
        return page(1, 5, nextPageToken());
    }

    public ActivityListResponse lastPage() {
        return page(1, 5, null);
    }

    public String nextPageToken() {
        return faker.regexify("[A-Za-z0-9_-]{6}");
    }

    public ActivityListResponse page(int minLength, int maxLength, String nextPageToken) {
        List<Activity> activities = faker.collection(() -> faker.youtube().activity().upload())
                .minLen(minLength)
                .maxLen(maxLength)
                .generate();

        var activityListResponse = new ActivityListResponse();
        activityListResponse.setItems(activities);
        activityListResponse.setNextPageToken(nextPageToken);
        return activityListResponse;
    }
}
