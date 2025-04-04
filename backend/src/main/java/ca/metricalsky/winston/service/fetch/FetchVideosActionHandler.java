package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.client.YouTubeClientAdapter;
import ca.metricalsky.winston.dto.VideoDto;
import ca.metricalsky.winston.entity.fetch.FetchAction;
import ca.metricalsky.winston.mapper.dto.VideoDtoMapper;
import ca.metricalsky.winston.mapper.entity.OffsetDateTimeMapper;
import ca.metricalsky.winston.mapper.entity.VideoMapper;
import ca.metricalsky.winston.repository.VideoRepository;
import com.google.api.services.youtube.model.Activity;
import com.google.api.services.youtube.model.ActivityListResponse;
import com.google.api.services.youtube.model.ActivitySnippet;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class FetchVideosActionHandler implements FetchActionHandler<VideoDto> {

    private final VideoMapper videoMapper = Mappers.getMapper(VideoMapper.class);
    private final VideoDtoMapper videoDtoMapper = Mappers.getMapper(VideoDtoMapper.class);
    private final OffsetDateTimeMapper offsetDateTimeMapper = new OffsetDateTimeMapper();
    private final VideoRepository videoRepository;
    private final YouTubeClientAdapter youTubeClientAdapter;

    @Override
    public FetchResult<VideoDto> fetch(FetchAction fetchAction) {
        var activityListResponse = youTubeClientAdapter.getActivities(fetchAction);
        var videoEntities = activityListResponse.getItems()
                .stream()
                .filter(activity -> activity.getContentDetails().getUpload() != null)
                .map(videoMapper::fromYouTube)
                .toList();
        videoRepository.saveAll(videoEntities);
        var videoDtos = videoEntities.stream()
                .map(videoDtoMapper::fromEntity)
                .toList();
        var nextFetchAction = getNextFetchAction(fetchAction, activityListResponse);
        return new FetchResult<>(fetchAction, videoDtos, nextFetchAction);
    }

    private FetchAction getNextFetchAction(FetchAction fetchAction, ActivityListResponse activityListResponse) {
        var activities = activityListResponse.getItems()
                .stream()
                .filter(activity -> activity.getContentDetails().getUpload() != null)
                .toList();
        if (activities.isEmpty()) {
            activities = activityListResponse.getItems();
        }
        var nextPublishedBefore = activities.stream()
                .map(Activity::getSnippet)
                .map(ActivitySnippet::getPublishedAt)
                .map(offsetDateTimeMapper::fromYouTube)
                .min(Comparator.naturalOrder())
                .map(publishedAt -> publishedAt.minusSeconds(1))
                .orElse(null);

        return activityListResponse.getNextPageToken() == null ? null : FetchAction.builder()
                .fetchRequestId(fetchAction.getFetchRequestId())
                .actionType(fetchAction.getActionType())
                .objectId(fetchAction.getObjectId())
                .publishedAfter(fetchAction.getPublishedAfter())
                .publishedBefore(nextPublishedBefore)
                .build();
    }
}
