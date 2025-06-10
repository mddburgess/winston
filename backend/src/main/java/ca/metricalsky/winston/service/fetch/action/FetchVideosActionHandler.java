package ca.metricalsky.winston.service.fetch.action;

import ca.metricalsky.winston.service.YouTubeService;
import ca.metricalsky.winston.dto.VideoDto;
import ca.metricalsky.winston.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.mapper.dto.VideoDtoMapper;
import ca.metricalsky.winston.mapper.entity.OffsetDateTimeMapper;
import ca.metricalsky.winston.mapper.entity.VideoEntityMapper;
import ca.metricalsky.winston.repository.VideoRepository;
import ca.metricalsky.winston.service.fetch.FetchActionService;
import ca.metricalsky.winston.service.fetch.FetchResult;
import com.google.api.services.youtube.model.Activity;
import com.google.api.services.youtube.model.ActivityListResponse;
import com.google.api.services.youtube.model.ActivitySnippet;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
public class FetchVideosActionHandler extends FetchActionHandler<VideoDto> {

    private final VideoEntityMapper videoEntityMapper = Mappers.getMapper(VideoEntityMapper.class);
    private final VideoDtoMapper videoDtoMapper = Mappers.getMapper(VideoDtoMapper.class);
    private final OffsetDateTimeMapper offsetDateTimeMapper = new OffsetDateTimeMapper();

    private final VideoRepository videoRepository;
    private final YouTubeService youTubeService;

    public FetchVideosActionHandler(
            FetchActionService fetchActionService,
            VideoRepository videoRepository,
            YouTubeService youTubeService
    ) {
        super(fetchActionService);
        this.videoRepository = videoRepository;
        this.youTubeService = youTubeService;
    }

    @Override
    protected FetchResult<VideoDto> doFetch(FetchActionEntity fetchAction) {
        var activityListResponse = youTubeService.getActivities(fetchAction);
        var videoEntities = activityListResponse.getItems()
                .stream()
                .filter(activity -> activity.getContentDetails().getUpload() != null)
                .map(videoEntityMapper::toVideoEntity)
                .toList();
        videoRepository.saveAll(videoEntities);
        var videoDtos = videoEntities.stream()
                .map(videoDtoMapper::fromEntity)
                .toList();
        var nextFetchAction = getNextFetchAction(fetchAction, activityListResponse);
        return new FetchResult<>(fetchAction, videoDtos, nextFetchAction);
    }

    private FetchActionEntity getNextFetchAction(FetchActionEntity fetchAction, ActivityListResponse activityListResponse) {
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

        return activityListResponse.getNextPageToken() == null ? null : FetchActionEntity.builder()
                .fetchOperationId(fetchAction.getFetchOperationId())
                .actionType(fetchAction.getActionType())
                .objectId(fetchAction.getObjectId())
                .publishedAfter(fetchAction.getPublishedAfter())
                .publishedBefore(nextPublishedBefore)
                .build();
    }
}
