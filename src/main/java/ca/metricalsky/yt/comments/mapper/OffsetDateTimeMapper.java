package ca.metricalsky.yt.comments.mapper;

import com.google.api.client.util.DateTime;
import org.mapstruct.Mapper;

import java.time.OffsetDateTime;

@Mapper
public class OffsetDateTimeMapper {

    public OffsetDateTime fromYouTube(DateTime dateTime) {
        return dateTime == null ? null : OffsetDateTime.parse(dateTime.toString());
    }
}
