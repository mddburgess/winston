package ca.metricalsky.winston.dao;

import ca.metricalsky.winston.database.entity.channel.ChannelEntity;
import ca.metricalsky.winston.entity.VideoEntity;
import ca.metricalsky.winston.entity.view.ChannelVideoView;
import ca.metricalsky.winston.database.view.VideoCountView;
import ca.metricalsky.winston.mappers.api.VideoMapper;
import ca.metricalsky.winston.mappers.api.VideoMapperImpl;
import ca.metricalsky.winston.repository.VideoRepository;
import ca.metricalsky.winston.test.TestUtils;
import ca.metricalsky.winston.test.faker.WinstonFaker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VideoDataServiceTest {

    private static final WinstonFaker faker = new WinstonFaker();
    private static final Sort publishedAtDesc = Sort.by(Sort.Direction.DESC, "publishedAt");

    @InjectMocks
    private VideoDataService videoDataService;

    @Spy
    private VideoMapper videoMapper = new VideoMapperImpl();
    @Mock
    private VideoRepository videoRepository;

    @Test
    void listVideosByChannelId() {
        var channelId = faker.youtube().channelId();
        var pageRequest = faker.page().pageRequest();
        var videoEntity = buildVideoEntity();

        when(videoRepository.findPageByChannelId(channelId, pageRequest.withSort(publishedAtDesc)))
                .thenReturn(List.of(videoEntity));

        var videos = videoDataService.listVideosByChannelId(channelId, pageRequest);

        assertThat(videos).first()
                .hasFieldOrPropertyWithValue("id", videoEntity.getId());
    }

    @Test
    void listVideosByChannelId_empty() {
        var channelId = faker.youtube().channelId();
        var pageRequest = faker.page().pageRequest();

        when(videoRepository.findPageByChannelId(channelId, pageRequest.withSort(publishedAtDesc)))
                .thenReturn(List.of());

        var videos = videoDataService.listVideosByChannelId(channelId, pageRequest);

        assertThat(videos)
                .isEmpty();
    }

    @Test
    void findVideoById() {
        var channelVideoView = mockChannelVideoView();
        var videoId = channelVideoView.getVideo().getId();

        when(videoRepository.findChannelVideoById(videoId))
                .thenReturn(Optional.of(channelVideoView));

        var video = videoDataService.findVideoById(videoId);

        assertThat(video).get()
                .hasFieldOrPropertyWithValue("id", videoId);
    }

    @Test
    void findVideoById_notFound() {
        var videoId = TestUtils.randomId();

        when(videoRepository.findChannelVideoById(videoId))
                .thenReturn(Optional.empty());

        var video = videoDataService.findVideoById(videoId);

        assertThat(video)
                .isEmpty();
    }

    @Test
    void getVideosForAuthor() {
        var channelVideoView = mockChannelVideoView();
        var videoId = channelVideoView.getVideo().getId();
        var authorHandle = TestUtils.randomString();

        when(videoRepository.findAllChannelVideosByAuthorDisplayName(authorHandle))
                .thenReturn(List.of(channelVideoView));

        var videos = videoDataService.getVideosForAuthor(authorHandle);

        assertThat(videos).first()
                .hasFieldOrPropertyWithValue("id", videoId);
    }

    @Test
    void getVideosForAuthor_empty() {
        var authorHandle = TestUtils.randomString();

        when(videoRepository.findAllChannelVideosByAuthorDisplayName(authorHandle))
                .thenReturn(List.of());

        var videos = videoDataService.getVideosForAuthor(authorHandle);

        assertThat(videos)
                .isEmpty();
    }

    @Test
    void countAllVideosByChannelId() {
        var videoCountView = mockVideoCountView();

        when(videoRepository.countAllGroupByChannelId())
                .thenReturn(List.of(videoCountView));

        var videoCounts = videoDataService.countAllVideosByChannelId();

        assertThat(videoCounts)
                .hasSize(1)
                .containsEntry(videoCountView.getChannelId(), videoCountView.getVideos());

    }

    @Test
    void countAllVideosByChannelId_defaultValue() {
        when(videoRepository.countAllGroupByChannelId())
                .thenReturn(List.of());

        var videoCounts = videoDataService.countAllVideosByChannelId();

        assertThat(videoCounts.get("defaultValue"))
                .isEqualTo(0);
    }

    private static VideoEntity buildVideoEntity() {
        return VideoEntity.builder()
                .id(TestUtils.randomId())
                .build();
    }

    private static ChannelVideoView mockChannelVideoView() {
        var channelVideoView = mock(ChannelVideoView.class);
        lenient().when(channelVideoView.getChannel())
                .thenReturn(new ChannelEntity());
        lenient().when(channelVideoView.getVideo())
                .thenReturn(buildVideoEntity());
        return channelVideoView;
    }

    private static VideoCountView mockVideoCountView() {
        var videoCountView = mock(VideoCountView.class);
        lenient().when(videoCountView.getChannelId())
                .thenReturn(TestUtils.randomId());
        lenient().when(videoCountView.getVideos())
                .thenReturn(1);
        return videoCountView;
    }
}
