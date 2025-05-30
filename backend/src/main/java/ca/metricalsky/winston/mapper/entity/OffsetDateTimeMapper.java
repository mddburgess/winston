package ca.metricalsky.winston.mapper.entity;

import com.google.api.client.util.DateTime;

import java.time.OffsetDateTime;

public class OffsetDateTimeMapper {

    public OffsetDateTime fromYouTube(DateTime dateTime) {
        return dateTime == null ? null : OffsetDateTime.parse(dateTime.toString());
    }
}
