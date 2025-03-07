package ca.metricalsky.yt.comments.mapper.entity;

import com.google.api.client.util.DateTime;

import java.time.OffsetDateTime;

class OffsetDateTimeMapper {

    OffsetDateTime fromYouTube(DateTime dateTime) {
        return dateTime == null ? null : OffsetDateTime.parse(dateTime.toString());
    }
}
