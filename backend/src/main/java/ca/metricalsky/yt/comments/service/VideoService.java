package ca.metricalsky.yt.comments.service;

import ca.metricalsky.yt.comments.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VideoService {

    private final VideoRepository videoRepository;

    @Autowired
    public VideoService(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    public Map<String, Long> countAllByChannelId() {
        return videoRepository.countAllByChannelId()
                .stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(0, String.class),
                        tuple -> tuple.get(1, Long.class)
                ));
    }
}
