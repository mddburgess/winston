package ca.metricalsky.yt.comments.mapper;

import com.google.api.client.util.DateTime;

import java.time.OffsetDateTime;

public class OffsetDateTimeMapper {

    public OffsetDateTime fromYouTube(DateTime dateTime) {
        return dateTime == null ? null : OffsetDateTime.parse(dateTime.toString());
    }

    public String fromEntity(OffsetDateTime offsetDateTime) {
        return offsetDateTime == null ? null : offsetDateTime.toString();
    }
}
