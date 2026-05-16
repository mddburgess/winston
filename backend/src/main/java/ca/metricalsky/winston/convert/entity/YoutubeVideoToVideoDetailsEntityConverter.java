package ca.metricalsky.winston.convert.entity;

import ca.metricalsky.winston.convert.ConversionServiceAdapter;
import ca.metricalsky.winston.database.entity.video.VideoContentRatingEntity;
import ca.metricalsky.winston.database.entity.video.VideoDetailsEntity;
import ca.metricalsky.winston.database.entity.video.VideoDetailsEntity.Visibility;
import ca.metricalsky.winston.database.entity.video.VideoRecordingLocationEntity;
import ca.metricalsky.winston.database.entity.video.VideoRestrictionEntity;
import ca.metricalsky.winston.database.entity.video.VideoRestrictionEntity.Restriction;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.youtube.model.GeoPoint;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoRecordingDetails;
import org.apache.commons.lang3.EnumUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

@Mapper(
        componentModel = "spring",
        uses = ConversionServiceAdapter.class
)
public abstract class YoutubeVideoToVideoDetailsEntityConverter
        implements Converter<Video, VideoDetailsEntity> {

    @Lazy
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @Mapping(target = "videoId", source = "id")
    @Mapping(target = "visibility", source = "status.privacyStatus")
    @Mapping(target = "duration", source = "contentDetails.duration")
    @Mapping(target = "category", source = "snippet.categoryId")
    @Mapping(target = "topics", source = "topicDetails.topicCategories")
    @Mapping(target = "tags", source = "snippet.tags")
    @Mapping(target = "restrictions", source = ".")
    @Mapping(target = "contentRatings", source = ".")
    @Mapping(target = "madeForKids", source = "status.madeForKids")
    @Mapping(target = "containsSyntheticMedia", source = "status.containsSyntheticMedia")
    @Mapping(target = "hasPaidProductPlacement", source = "paidProductPlacementDetails.hasPaidProductPlacement")
    @Mapping(target = "recordingLocation", source = ".")
    @Mapping(target = "recordedAt", source = "recordingDetails.recordingDate")
    @Mapping(target = "liveStreamedAt", source = "liveStreamingDetails.actualStartTime")
    @Mapping(target = "viewCount", source = "statistics.viewCount")
    @Mapping(target = "likeCount", source = "statistics.likeCount")
    @Mapping(target = "commentCount", source = "statistics.commentCount")
    public abstract VideoDetailsEntity convert(Video source);

    Visibility convertPrivacyStatus(String privacyStatus) {
        return privacyStatus == null ? null
                : EnumUtils.getEnum(Visibility.class, privacyStatus.toUpperCase(Locale.ENGLISH));
    }

    List<VideoRestrictionEntity> convertRegionRestriction(Video source) {
        if (source.getContentDetails() == null || source.getContentDetails().getRegionRestriction() == null) {
            return List.of();
        }

        var sourceRegionRestriction = source.getContentDetails().getRegionRestriction();
        var restriction = isNotEmpty(sourceRegionRestriction.getAllowed())
                ? Restriction.ALLOWED
                : Restriction.BLOCKED;
        var countries = isNotEmpty(sourceRegionRestriction.getAllowed())
                ? sourceRegionRestriction.getAllowed()
                : firstNonNull(sourceRegionRestriction.getBlocked(), List.<String>of());

        return countries.stream().map(country -> {
                    var videoRestriction = new VideoRestrictionEntity();
                    videoRestriction.setVideoId(source.getId());
                    videoRestriction.setRestriction(restriction);
                    videoRestriction.setCountry(country);
                    return videoRestriction;
                }
        ).toList();
    }

    List<VideoContentRatingEntity> convertContentRating(Video source) {
        if (source.getContentDetails() == null || source.getContentDetails().getContentRating() == null) {
            return List.of();
        }

        var sourceContentRating = objectMapper.valueToTree(source.getContentDetails().getContentRating());
        var contentRatings = new ArrayList<VideoContentRatingEntity>();

        sourceContentRating.fieldNames().forEachRemaining(fieldName -> {
            if (fieldName.endsWith("Rating") && sourceContentRating.get(fieldName) != null) {
                var authority = fieldName.substring(0, fieldName.indexOf("Rating"));
                var rating = sourceContentRating.get(fieldName).textValue();

                var videoContentRating = new VideoContentRatingEntity();
                videoContentRating.setVideoId(source.getId());
                videoContentRating.setAuthority(authority);
                videoContentRating.setRating(rating);
                contentRatings.add(videoContentRating);
            }
        });

        return contentRatings;
    }

    VideoRecordingLocationEntity convertRecordingDetails(Video source) {
        var latitude = Optional.ofNullable(source.getRecordingDetails())
                .map(VideoRecordingDetails::getLocation)
                .map(GeoPoint::getLatitude)
                .orElse(null);
        var longitude = Optional.ofNullable(source.getRecordingDetails())
                .map(VideoRecordingDetails::getLocation)
                .map(GeoPoint::getLongitude)
                .orElse(null);

        if (latitude == null || longitude == null) {
            return null;
        }

        var videoRecordingLocation = new VideoRecordingLocationEntity();
        videoRecordingLocation.setVideoId(source.getId());
        videoRecordingLocation.setDescription(source.getRecordingDetails().getLocationDescription());
        videoRecordingLocation.setLatitude(latitude);
        videoRecordingLocation.setLongitude(longitude);
        videoRecordingLocation.setAltitude(source.getRecordingDetails().getLocation().getAltitude());
        return videoRecordingLocation;
    }
}
